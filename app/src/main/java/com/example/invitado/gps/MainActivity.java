package com.example.invitado.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView txtTotal;
    LocationManager gps;
    EditText banderazo;
    EditText precioMetro;
    Button botonInicio;
    Button botonFinal;

    double r, latitud, longitud, latitud2, longitud2, diferenciaLatitud,
            diferenciaLongitud, total, radOrig, radDes, num1, num2;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTotal = (TextView) findViewById(R.id.txtTotal);
        banderazo = (EditText) findViewById(R.id.banderazo);
        precioMetro = (EditText) findViewById(R.id.precioMetro);
        botonFinal = (Button) findViewById(R.id.botonFinal);
        botonInicio = (Button) findViewById(R.id.botonInicio);
        gps = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gps.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 250, this);


    }

    @Override
    public void onLocationChanged(final Location location) {


        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String nu1=banderazo.getText().toString();
            String nu2=precioMetro.getText().toString();
            num1 = Double.parseDouble(nu1);
            num2=Double.parseDouble(nu2);

                latitud = location.getLatitude();
                longitud = location.getLongitude();
                latitud2=latitud;
                longitud2=longitud;
                total=num1;
                txtTotal.setText(""+total);
                r=0;
                flag=true;


            }
        });
        if(flag==true) {
            latitud = latitud2;
            longitud = longitud2;
            latitud2 = location.getLatitude();
            longitud2 = location.getLongitude();

            if (latitud2 == latitud && longitud2 == longitud) {
                r = 0;
            } else {


                Log.i("Datos", "Ubicacion Latitud: " + latitud + "Longitud: " + longitud +
                        "Ubicacion Latitud2: " + latitud2 + "Longitud2: " + longitud2);

                radOrig = ((latitud) * Math.PI) / 180;
                radDes = ((latitud2) * Math.PI) / 180;
                diferenciaLatitud = ((latitud2 - latitud) * Math.PI) / 180;
                diferenciaLongitud = ((longitud2 - longitud) * Math.PI) / 180;

                r = getDistanceBetweenTwoPoints(diferenciaLatitud, diferenciaLongitud, radDes, radOrig);
                total = total + (r * num2);
                txtTotal.setText("Total " + total);
            }
        }
        botonFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTotal.setText("Total a pagar " + total);
                flag=false;
            }
        });
}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public Double getDistanceBetweenTwoPoints (Double diferenciaLatitud, Double diferenciaLongitud, Double radDes,
                                               Double radOrig){

            int earth = 6371;

            double a = Math.sin(diferenciaLatitud / 2) * Math.sin(diferenciaLatitud / 2)
                    + Math.cos(radOrig) * Math.cos(radDes) * Math.sin(diferenciaLongitud / 2)
                    * Math.sin(diferenciaLongitud / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return (earth * c) * 1000;

    }
}



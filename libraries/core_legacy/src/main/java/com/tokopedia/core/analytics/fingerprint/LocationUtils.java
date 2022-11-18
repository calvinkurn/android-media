package com.tokopedia.core.analytics.fingerprint;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;

public class LocationUtils {
    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    public LocationUtils(Context ctx) {
        context = ctx;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void initLocationBackground() {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        saveLocation(location);
                    } else {
                        fusedLocationClient.requestLocationUpdates(new LocationRequest(), getLocationCallback(), Looper.getMainLooper());
                    }
                });
            }
        } catch (Exception ignored) {
        }
    }

    private void saveLocation(@NonNull Location location) {
        FingerprintModelGenerator.INSTANCE.expireFingerprint();
        new LocationCache(context).saveLocation(context, location);
    }

    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location lastLocation = locationResult.getLastLocation();
                if (lastLocation != null) {
                    saveLocation(lastLocation);
                    fusedLocationClient.removeLocationUpdates(this);
                }
            }
        };
    }
}

package com.tokopedia.core.analytics.fingerprint;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator;

public class LocationUtils {
    private final Context context;

    public LocationUtils(Context ctx) {
        context = ctx;
    }

    public void initLocationBackground() {
        try {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    FingerprintModelGenerator.INSTANCE.expireFingerprint();
                    new LocationCache(context).saveLocation(context, location);
                });
            }
        } catch (SecurityException ignored) {
        }
    }
}

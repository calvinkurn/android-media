package com.tokopedia.core.analytics.fingerprint;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.analytics.fingerprint.domain.usecase.CacheGetFingerprintUseCase;
import com.tokopedia.core.deprecated.LocalCacheHandler;

import static com.tokopedia.core.analytics.fingerprint.LocationCache.DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.tokopedia.core.analytics.fingerprint.LocationCache.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;

/**
 * Created by Herdi_WORK on 22.06.17.
 */

public class LocationUtils implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Context context;
    boolean isConnected;
    private LocalCacheHandler localCacheHandler;

    public LocationUtils(Context ctx) {
        localCacheHandler = new LocalCacheHandler(ctx, CacheGetFingerprintUseCase.FINGERPRINT_KEY_NAME);
        context = ctx;
    }

    public void initLocationBackground() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        locationRequest = LocationRequest.create().setInterval(DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        isConnected = false;
    }

    public void deInitLocationBackground() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        localCacheHandler.clearCache(CacheGetFingerprintUseCase.FINGERPRINT_USE_CASE);
        new LocationCache(context).saveLocation(context, location);
        removeLocationUpdates();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = true;
        getLastLocation();
        requestLocationUpdates();
    }

    private void getLastLocation() {
        if (isLocationServiceConnected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    if (location != null) {
                        new LocationCache(context).saveLocation(context, location);
                    } else {
                        CommonUtils.dumper("location permission not granted");
                    }
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean isLocationServiceConnected() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(context);

        if (ConnectionResult.SUCCESS == resultCode) {
            CommonUtils.dumper("Google play services available");
            return true;
        } else {
            CommonUtils.dumper("Google play services unavailable");
            return false;
        }
    }

    private void requestLocationUpdates() {
        if (googleApiClient.isConnected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        ) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }
            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }

        }
    }

    private void removeLocationUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }
}

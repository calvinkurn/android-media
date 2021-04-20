package com.tokopedia.core.analytics.fingerprint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tokopedia.core.analytics.fingerprint.domain.usecase.CacheGetFingerprintUseCase;
import com.tokopedia.core.deprecated.LocalCacheHandler;

import timber.log.Timber;

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
                        Timber.d("location permission not granted");
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
            Timber.d("Google play services available");
            return true;
        } else {
            Timber.d("Google play services unavailable");
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        if (googleApiClient.isConnected()) {
            // follow the same logic in here: https://github.com/mapbox/mapbox-events-android/pull/184/files
            // fix fabric bug: https://fabric.io/pt-tokopedia/android/apps/com.tokopedia.tkpd/issues/5c305bc7f8b88c29633eef60?time=last-seven-days
            // add locationRequest!= null to "fix Attempt to read from field 'int com.google.android.gms.location.LocationRequest.a' on a null object reference"
            if (locationPermissionCheck() && locationRequest != null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
        }
    }

    private boolean locationPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;
        } else {
            int coarsePermission = PermissionChecker.checkSelfPermission(context.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            int finePermission = PermissionChecker.checkSelfPermission(context.getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);

            return coarsePermission == PackageManager.PERMISSION_GRANTED
                    || finePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void removeLocationUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }
}

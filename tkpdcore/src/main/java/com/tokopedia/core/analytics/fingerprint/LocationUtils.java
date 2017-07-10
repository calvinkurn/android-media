package com.tokopedia.core.analytics.fingerprint;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.tokopedia.core.geolocation.presenter.GoogleMapPresenter.DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.tokopedia.core.geolocation.presenter.GoogleMapPresenter.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;

/**
 * Created by Herdi_WORK on 22.06.17.
 */

public class LocationUtils implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final GoogleApiClient googleApiClient;
    private final LocationRequest locationRequest;
    private Context context;
    boolean isConnected;

    public LocationUtils(Context ctx){

        context = ctx;

        googleApiClient = new GoogleApiClient.Builder(ctx)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = LocationRequest.create().setInterval(DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        isConnected = false;

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.telephony.CellLocation.requestLocationUpdate
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.usecase.AutocompleteGeocodeUseCase
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */

@AddNewAddressScope
class MapPresenter @Inject constructor(private val context: Context):
        BaseDaggerPresenter<AddNewAddressView>(), LocationListener {
    var googleApiClient: GoogleApiClient? = null

    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }

    fun connectGoogleApi(mapFragment: MapFragment) {
        this.googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(mapFragment)
                .addOnConnectionFailedListener(mapFragment)
                .build()

        this.googleApiClient?.connect()
    }

    fun disconnectGoogleApi() {
        if (googleApiClient?.isConnected!!) {
            googleApiClient?.disconnect()
        }
    }

    fun onResult(locationSettingResult: LocationSettingsResult) {
        val status: Status = locationSettingResult.status
        when(status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS -> {
                view.moveMap(MapUtils.generateLatLng(defaultLat, defaultLong))
            }
        }

        /*final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
            view.moveMap(getLastLocation());
            requestLocationUpdate();
            break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
            view.showDialogError(status);
            break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
            break;
            default:
            break;
        }*/
    }

    override fun onLocationChanged(p0: Location?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*@SuppressLint("MissingPermission")
    private fun getLastLocation(): LatLng {
        return try {
            if (isServiceConnected()) {
                val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                MapUtils.saveLocation(context, location)
                LatLng(location.latitude, location.longitude)
            } else {
                MapUtils.generateLatLng(defaultLat, defaultLong)
            }
        } catch (e: Exception) {
            MapUtils.generateLatLng(defaultLat, defaultLong)
        }

    }*/

    private fun isServiceConnected(): Boolean {
        val availability = GoogleApiAvailability.getInstance()

        val resultCode = availability.isGooglePlayServicesAvailable(context)

        return if (ConnectionResult.SUCCESS == resultCode) {
            CommonUtils.dumper("Google play services available")
            true
        } else {
            CommonUtils.dumper("Google play services unavailable")
            false
        }
    }
}
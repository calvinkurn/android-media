package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
class AddNewAddressPresenter @Inject constructor(private val context: Context) {
    var googleApiClient: GoogleApiClient? = null

    fun connectGoogleApi(addNewAddressFragment: AddNewAddressFragment) {
        this.googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(addNewAddressFragment)
                .addOnConnectionFailedListener(addNewAddressFragment)
                .build()

        this.googleApiClient?.connect()
    }
}
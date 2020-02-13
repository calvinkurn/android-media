package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.location_info

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.OnFailureListener
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics

/**
 * Created by fwidjaja on 2019-06-18.
 */
class LocationInfoBottomSheetFragment : BottomSheets() {
    private var bottomSheetView: View? = null
    private lateinit var btnActivateLocation: ButtonCompat
    private var isFullFlow: Boolean = true

    companion object {
        @JvmStatic
        fun newInstance(isFullFlow: Boolean): LocationInfoBottomSheetFragment {
            return LocationInfoBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_FULL_FLOW, isFullFlow)
                }
            }
        }
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_location_info
    }

    override fun initView(view: View) {
        bottomSheetView = view
        btnActivateLocation = view.findViewById(R.id.btn_activate_location)
        btnActivateLocation.setOnClickListener {
            if(isFullFlow) AddNewAddressAnalytics.eventClickButtonAktifkanLayananLokasiOnBlockGps(eventLabel = LOGISTIC_LABEL)
            else AddNewAddressAnalytics.eventClickButtonAktifkanLayananLokasiOnBlockGps(eventLabel = NON_LOGISTIC_LABEL)
            if (!context?.let { it1 -> turnGPSOn(it1) }!!) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            dismiss()
        }
    }

    override fun title(): String {
        return getString(R.string.undetected_location)
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(R.id.btn_close)?.setOnClickListener {
            if(isFullFlow) AddNewAddressAnalytics.eventClickButtonXOnBlockGps(eventLabel = LOGISTIC_LABEL)
            else AddNewAddressAnalytics.eventClickButtonXOnBlockGps(eventLabel = NON_LOGISTIC_LABEL)
            onCloseButtonClick()
        }
    }

    private fun turnGPSOn(context: Context): Boolean {
        var isGpsOn = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mSettingsClient = LocationServices.getSettingsClient(context)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000
        locationRequest.fastestInterval = 2 * 1000
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGpsOn = true
        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(context as Activity) {
                        //  GPS is already enable, callback GPS status through listener
                        isGpsOn = true
                    }
                    .addOnFailureListener(context, OnFailureListener { e ->
                        when ((e as ApiException).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(context, AddressConstants.GPS_REQUEST)
                                } catch (sie: IntentSender.SendIntentException) {
                                    sie.printStackTrace()
                                }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
        }
        return isGpsOn
    }
}
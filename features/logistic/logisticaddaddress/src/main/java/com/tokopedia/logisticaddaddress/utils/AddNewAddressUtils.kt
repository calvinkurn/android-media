package com.tokopedia.logisticaddaddress.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.getInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DENIED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DONT_ASK_AGAIN
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_GRANTED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_NOT_DEFINED
import kotlin.math.abs

/**
 * Created by fwidjaja on 2019-06-22.
 */
object AddNewAddressUtils {

    private const val MAX_LINES = 5
    private const val LOCATION_REQUEST_INTERVAL = 10000L
    private const val COORDINATE_THRESHOLD = 0.00001
    private const val LOCATION_REQUEST_FASTEST_INTERVAL = 2000L

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun isLocationEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            val mode = getInt(context.contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }

    @JvmStatic
    fun isGpsEnabled(context: Context?): Boolean {
        var isGpsOn = false
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val mSettingsClient = LocationServices.getSettingsClient(it)

            val mLocationSettingsRequest = requestLocationBuilder()?.build()
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                isGpsOn = true
            } else {
                mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(context as Activity) {
                        isGpsOn = true
                    }
            }

            isGpsOn = isLocationEnabled(it) && isGpsOn
        }
        return isGpsOn
    }

    private fun requestLocationBuilder(): LocationSettingsRequest.Builder? {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(getLocationRequest())
        builder.setAlwaysShow(true)
        return builder
    }

    @JvmStatic
    fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = LOCATION_REQUEST_INTERVAL
        locationRequest.fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
        return locationRequest
    }

    fun hasDefaultCoordinate(lat: Double, long: Double, exact: Boolean = false): Boolean {
        val threshold = COORDINATE_THRESHOLD
        val diffLat = lat - AddressConstants.DEFAULT_LAT
        val diffLong = long - AddressConstants.DEFAULT_LONG

        return if (exact) {
            (diffLat == 0.0 && diffLong == 0.0)
        } else {
            (abs(diffLat) < threshold && abs(diffLong) < threshold)
        }
    }

    fun getPermissionStateFromResult(activity: Activity, context: Context, permissions: Array<out String>): Int {
        var state = PERMISSION_NOT_DEFINED
        for (permission in permissions) {
            state = when {
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                    PERMISSION_GRANTED
                }
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                    PERMISSION_DENIED
                }
                else -> {
                    PERMISSION_DONT_ASK_AGAIN
                }
            }
        }
        return state
    }
}

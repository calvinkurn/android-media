package com.tokopedia.locationmanager

import android.app.Activity
import com.google.android.gms.location.FusedLocationProviderClient
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 25/01/19.
 */
class LocationDetectorHelper @Inject constructor(private val permissionCheckerHelper: PermissionCheckerHelper,
                                                 private val fusedLocationProvider: FusedLocationProviderClient) {

    fun getLocation(onGetLocation: ((DeviceLocation) -> Unit),
                    activity: Activity) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            permissionCheckerHelper.checkPermissions(activity, getPermissions(),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            permissionCheckerHelper.onPermissionDenied(activity, permissionText)
                            onGetLocation(DeviceLocation())
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper.onNeverAskAgain(activity, permissionText)
                            onGetLocation(DeviceLocation())
                        }

                        override fun onPermissionGranted() {
                            getLatitudeLongitude(onGetLocation, activity)
                        }

                    })
    }

    @SuppressWarnings("MissingPermission")
    private fun getLatitudeLongitude(onGetLocation: (DeviceLocation) -> Unit,
                                     activity: Activity) {
        fusedLocationProvider.lastLocation.addOnSuccessListener(activity) { location ->
            if (location != null) {
                val wayLatitude = location.latitude
                val wayLongitude = location.longitude
                onGetLocation(DeviceLocation(wayLatitude, wayLongitude, Date().time))
            }
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
    }
}
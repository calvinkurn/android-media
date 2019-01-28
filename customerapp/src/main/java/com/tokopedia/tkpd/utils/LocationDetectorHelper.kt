package com.tokopedia.tkpd.utils

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.PermissionCheckerHelper
import javax.inject.Inject

/**
 * @author by nisie on 25/01/19.
 */
class LocationDetectorHelper @Inject constructor(@ApplicationContext val applicationContext: Context,
                                                 val permissionCheckerHelper: PermissionCheckerHelper,
                                                 private val fusedLocationProvider: FusedLocationProviderClient) {

    fun getLocation(onGetLocation: ((Double, Double) -> Unit),
                    activity: Activity) {

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        permissionCheckerHelper.checkPermissions(applicationContext, getPermissions(),
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissions: Array<String>) {
                        onGetLocation(0.0, 0.0)
                    }

                    override fun onNeverAskAgain(permissions: Array<String>) {
//                        Toast.makeText()
                        onGetLocation(0.0, 0.0)
                    }

                    override fun onPermissionGranted() {
                        getLatitudeLongitude(onGetLocation, activity)
                    }

                })
    }

    @SuppressWarnings("MissingPermission")
    private fun getLatitudeLongitude(onGetLocation: (Double, Double) -> Unit,
                                     activity: Activity) {
        fusedLocationProvider.lastLocation.addOnSuccessListener(activity) { location ->
            if (location != null) {
                val wayLatitude = location.latitude
                val wayLongitude = location.longitude
                onGetLocation(wayLatitude, wayLongitude)
            }
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
    }
}
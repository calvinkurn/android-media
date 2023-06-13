package com.tokopedia.locationmanager

import android.app.Activity
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.util.*

/**
 * @author by nisie on 25/01/19.
 */
class LocationDetectorHelper constructor(private val permissionCheckerHelper: PermissionCheckerHelper,
                                                 private val fusedLocationProvider:
                                                 FusedLocationProviderClient,
                                                 applicationContext: Context) {

    companion object {
        const val TYPE_DEFAULT_FROM_CLOUD: Int = 1
        const val TYPE_DEFAULT_FROM_LOCAL: Int = 2

        /**
         * @param requestLocationType to determine location type that you need
         */
        fun getPermissions(requestLocationType: RequestLocationType): Array<String> {
            return when(requestLocationType){
                RequestLocationType.APPROXIMATE -> arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
                RequestLocationType.PRECISE -> arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION)
                RequestLocationType.APPROXIMATE_OR_PRECISE -> arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
            }
        }
    }

    private val LOCATION_CACHE: String = "LOCATION_CACHE"
    private val PARAM_CACHE_DEVICE_LOCATION: String = "DEVICE_LOCATION"

    private val cacheManager = PersistentCacheManager(applicationContext, LOCATION_CACHE)

    fun getLocation(onGetLocation: ((DeviceLocation) -> Unit),
                    activity: Activity,
                    type: Int = TYPE_DEFAULT_FROM_CLOUD,
                    requestLocationType: RequestLocationType = RequestLocationType.APPROXIMATE_OR_PRECISE,
                    rationaleText: String = "") {

        when (type) {
            TYPE_DEFAULT_FROM_CLOUD -> getDataFromCloud(onGetLocation, activity, requestLocationType, rationaleText)
            TYPE_DEFAULT_FROM_LOCAL -> getDataFromLocal(onGetLocation)
        }
    }

    private fun getDataFromLocal(onGetLocation: (DeviceLocation) -> Unit) {
        val deviceLocation = cacheManager.get(PARAM_CACHE_DEVICE_LOCATION, DeviceLocation::class
                .java, DeviceLocation())

        if (deviceLocation != null) {
            onGetLocation(deviceLocation)
        } else {
            onGetLocation(DeviceLocation())
        }
    }

    private fun getDataFromCloud(
        onGetLocation: (DeviceLocation) -> Unit,
        activity: Activity,
        requestLocationType: RequestLocationType,
        rationaleText: String = "") {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.checkPermissions(activity, getPermissions(requestLocationType),
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

                    },
                    rationaleText)
        } else {
            getLatitudeLongitude(onGetLocation, activity)
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun getLatitudeLongitude(onGetLocation: (DeviceLocation) -> Unit,
                                     activity: Activity) {
        fusedLocationProvider.lastLocation.addOnSuccessListener(activity) { location ->
            if (location != null) {
                val wayLatitude = location.latitude
                val wayLongitude = location.longitude
                val deviceLocation = DeviceLocation(wayLatitude, wayLongitude, Date().time)
                saveToCache(deviceLocation)
                onGetLocation(deviceLocation)
            } else {
                onErrorGetLocation(onGetLocation)
            }
        }
        fusedLocationProvider.lastLocation.addOnFailureListener(activity) { exception ->
            run {
                onErrorGetLocation(onGetLocation)
            }
        }
    }

    private fun onErrorGetLocation(onGetLocation: (DeviceLocation) -> Unit) {
        val deviceLocation = DeviceLocation(0.0, 0.0, Date().time)
        onGetLocation(deviceLocation)
    }

    private fun saveToCache(deviceLocation: DeviceLocation) {
        cacheManager.put(PARAM_CACHE_DEVICE_LOCATION, deviceLocation)
    }
}
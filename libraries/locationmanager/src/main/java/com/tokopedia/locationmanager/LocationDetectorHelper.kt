package com.tokopedia.locationmanager

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.locationmanager.legacy.LocationCache
import com.tokopedia.locationmanager.legacy.LocationCache.DEFAULT_LATITUDE
import com.tokopedia.locationmanager.legacy.LocationCache.DEFAULT_LONGITUDE
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

/**
 * @author by nisie on 25/01/19.
 */
class LocationDetectorHelper(ctx: Context): CoroutineScope {

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val context: Context

    init {
        context = ctx.applicationContext
    }

    fun getProvider(): FusedLocationProviderClient {
        return fusedLocationProvider
            ?: return LocationServices.getFusedLocationProviderClient(context).also {
                fusedLocationProvider = it
            }
    }

    companion object {
        const val TYPE_DEFAULT_FROM_CLOUD: Int = 1
        const val TYPE_DEFAULT_FROM_LOCAL: Int = 2

        const val LOCATION_CACHE: String = "LOCATION_CACHE"
        const val PARAM_CACHE_DEVICE_LOCATION: String = "DEVICE_LOCATION"

        /**
         * @param requestLocationType to determine location type that you need
         */
        fun getPermissions(requestLocationType: RequestLocationType): Array<String> {
            return when (requestLocationType) {
                RequestLocationType.APPROXIMATE -> arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION
                )

                RequestLocationType.PRECISE -> arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION
                )

                RequestLocationType.APPROXIMATE_OR_PRECISE -> arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    private val cacheManager = PersistentCacheManager(context, LOCATION_CACHE)

    /**
     * request location with asking permission
     * if permission is not granted, user will be asked for permission
     * See getLocationSuspend if doing this in coroutine
     */
    fun getLocation(
        onGetLocation: ((DeviceLocation) -> Unit)?,
        activity: Activity,
        type: Int = TYPE_DEFAULT_FROM_CLOUD,
        requestLocationType: RequestLocationType = RequestLocationType.APPROXIMATE_OR_PRECISE,
        permissionCheckerHelper: PermissionCheckerHelper,
        rationaleText: String = ""
    ) {

        when (type) {
            TYPE_DEFAULT_FROM_CLOUD -> getDataFromCloud(
                onGetLocation,
                activity,
                requestLocationType,
                permissionCheckerHelper,
                rationaleText
            )

            else -> getDataFromLocal(onGetLocation)
        }
    }

    /**
     * request location without asking permission, therefore no activity is required.
     * See getLocationSuspend if doing this in coroutine
     */
    fun getLocation(
        onGetLocation: ((DeviceLocation) -> Unit)?,
        type: Int = TYPE_DEFAULT_FROM_CLOUD,
        requestLocationType: RequestLocationType = RequestLocationType.APPROXIMATE_OR_PRECISE,
    ) {

        when (type) {
            TYPE_DEFAULT_FROM_CLOUD -> getDataFromCloud(
                onGetLocation,
                context.applicationContext,
                requestLocationType
            )

            else -> getDataFromLocal(onGetLocation)
        }
    }

    /**
     * request location without asking permission, therefore no activity is required.
     */
    suspend fun getLocationSuspend(
        type: Int = TYPE_DEFAULT_FROM_CLOUD,
        requestLocationType: RequestLocationType = RequestLocationType.APPROXIMATE_OR_PRECISE,
    ): DeviceLocation {

        return when (type) {
            TYPE_DEFAULT_FROM_CLOUD -> getDataFromCloud(
                context.applicationContext,
                requestLocationType
            )

            else -> {
                getDataFromLocal()
            }
        }
    }

    /**
     * save current location to cache
     */
    fun saveToCache(latitude: Double, longitude: Double) {
        saveToCache(DeviceLocation(latitude, longitude, Date().time))
    }

    fun getLocationCache(): DeviceLocation {
        return getDataFromLocal()
    }

    /**
     * getDataFromLocal with callback result
     */
    private fun getDataFromLocal(onGetLocation: ((DeviceLocation) -> Unit)?) {
        val deviceLocation = getDataFromLocal()
        onGetLocation?.invoke(deviceLocation)
    }

    /**
     * getDataFromLocal with direct result
     */
    private fun getDataFromLocal(): DeviceLocation {
        val location = cacheManager.get(
            PARAM_CACHE_DEVICE_LOCATION, DeviceLocation::class
                .java, DeviceLocation()
        )
        return if (location == null || !location.hasLocation()) {
            // Previously saved into shared Preference.
            // TODO will be removed after several releases
            val latLegacy = LocationCache.getLatitudeCache(context)
            val lonLegacy = LocationCache.getLongitudeCache(context)
            if (latLegacy == DEFAULT_LATITUDE.toString() && lonLegacy == DEFAULT_LONGITUDE.toString()) {
                DeviceLocation()
            } else {
                val locationFromLegacy =
                    DeviceLocation(latLegacy.toDouble(), lonLegacy.toDouble(), Date().time)
                saveToCache(locationFromLegacy)
                locationFromLegacy
            }
        } else {
            location
        }
    }

    private fun getDataFromCloud(
        onGetLocation: ((DeviceLocation) -> Unit)?,
        activity: Activity,
        requestLocationType: RequestLocationType,
        permissionCheckerHelper: PermissionCheckerHelper,
        rationaleText: String = ""
    ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.checkPermissions(
                activity, getPermissions(requestLocationType),
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        permissionCheckerHelper.onPermissionDenied(activity, permissionText)
                        onGetLocation?.invoke(DeviceLocation())
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        permissionCheckerHelper.onNeverAskAgain(activity, permissionText)
                        onGetLocation?.invoke(DeviceLocation())
                    }

                    override fun onPermissionGranted() {
                        getLatitudeLongitude(onGetLocation, activity)
                    }

                },
                rationaleText
            )
        } else {
            getLatitudeLongitude(onGetLocation, activity)
        }
    }

    private fun getDataFromCloud(
        onGetLocation: ((DeviceLocation) -> Unit)?,
        context: Context,
        requestLocationType: RequestLocationType
    ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            var grantedCount = 0
            var totalCount: Int
            getPermissions(requestLocationType).apply {
                totalCount = this.size
                onEach {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        grantedCount++
                    }
                }
            }
            if (grantedCount == totalCount) {
                getLatitudeLongitude(onGetLocation)
            }
        } else {
            getLatitudeLongitude(onGetLocation)
        }
    }

    private suspend fun getDataFromCloud(
        context: Context,
        requestLocationType: RequestLocationType
    ): DeviceLocation {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            var grantedCount = 0
            var totalCount: Int
            getPermissions(requestLocationType).apply {
                totalCount = this.size
                onEach {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        grantedCount++
                    }
                }
            }
            if (grantedCount == totalCount) {
                getLatitudeLongitudeSuspend()
            } else {
                DeviceLocation()
            }
        } else {
            getLatitudeLongitudeSuspend()
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun getLatitudeLongitude(
        onGetLocation: ((DeviceLocation) -> Unit)?,
        activity: Activity
    ) {
        getProvider().lastLocation.addOnSuccessListener(activity) { location ->
            if (location != null) {
                onSuccessGetLocation(location, onGetLocation)
            } else {
                if (activity.isDestroyed || activity.isFinishing) {
                    return@addOnSuccessListener
                }
                getProvider().requestLocationUpdates(
                    LocationRequest(),
                    getLocationCallback(onGetLocation),
                    Looper.getMainLooper()
                )
            }
        }.addOnFailureListener(activity) {
            onErrorGetLocation(onGetLocation)
        }
    }

    /**
     * This function is to get lat long without activity
     */
    @SuppressWarnings("MissingPermission")
    private fun getLatitudeLongitude(onGetLocation: ((DeviceLocation) -> Unit)?) {
        getProvider().lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onSuccessGetLocation(location, onGetLocation)
            } else {
                getProvider().requestLocationUpdates(
                    LocationRequest(),
                    getLocationCallback(onGetLocation),
                    Looper.getMainLooper()
                )
            }
        }.addOnFailureListener {
            onErrorGetLocation(onGetLocation)
        }
    }

    @SuppressWarnings("MissingPermission")
    private suspend fun getLatitudeLongitudeSuspend(): DeviceLocation {
        return suspendCancellableCoroutine { cont ->
            // https://stackoverflow.com/questions/48227346/
            var hasResumed = false
            getProvider().lastLocation.addOnSuccessListener { location ->
                if (!hasResumed) {
                    if (location != null) {
                        val deviceLocation = onSuccessGetLocation(location)
                        cont.resume(deviceLocation)
                    } else {
                        getProvider().requestLocationUpdates(
                            LocationRequest(),
                            getLocationCallback(cont),
                            Looper.getMainLooper()
                        )
                    }
                    hasResumed = true
                }
            }.addOnFailureListener {
                if (!hasResumed) {
                    cont.resume(DeviceLocation(0.0, 0.0, Date().time))
                    hasResumed = true
                }
            }
        }
    }

    private fun onSuccessGetLocation(
        location: Location,
        onGetLocation: ((DeviceLocation) -> Unit)? = null
    ): DeviceLocation {
        val wayLatitude = location.latitude
        val wayLongitude = location.longitude
        val deviceLocation = DeviceLocation(wayLatitude, wayLongitude, Date().time)
        saveToCache(deviceLocation)
        onGetLocation?.invoke(deviceLocation)
        return deviceLocation
    }

    private fun getLocationCallback(onGetLocation: ((DeviceLocation) -> Unit)?): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lastLocation = locationResult.lastLocation
                if (lastLocation != null) {
                    onSuccessGetLocation(lastLocation, onGetLocation)
                } else {
                    onErrorGetLocation(onGetLocation)
                }
                getProvider().removeLocationUpdates(this)
            }
        }
    }

    private fun getLocationCallback(continuation: CancellableContinuation<DeviceLocation>?): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lastLocation = locationResult.lastLocation
                if (lastLocation != null) {
                    val loc = onSuccessGetLocation(lastLocation, null)
                    getProvider().removeLocationUpdates(this)
                    continuation?.resume(loc)
                }
            }
        }
    }

    private fun onErrorGetLocation(onGetLocation: ((DeviceLocation) -> Unit)?) {
        val deviceLocation = DeviceLocation()
        onGetLocation?.invoke(deviceLocation)
    }

    private fun saveToCache(deviceLocation: DeviceLocation) {
        launch {
            cacheManager.put(PARAM_CACHE_DEVICE_LOCATION, deviceLocation, TimeUnit.DAYS.toMillis(365))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }

}
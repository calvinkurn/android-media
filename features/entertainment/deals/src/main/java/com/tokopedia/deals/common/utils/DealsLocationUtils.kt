package com.tokopedia.deals.common.utils

import android.app.Activity
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.deals.common.listener.CurrentLocationCallback
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper

/**
 * @author by jessica on 15/06/20
 */

class DealsLocationUtils(val context: Context) {

    fun getLocation(): Location {
        val localCacheHandler = LocalCacheHandler(context, TkpdCache.DEALS_LOCATION)
        val locationJson = localCacheHandler.getString(TkpdCache.Key.KEY_DEALS_LOCATION, null)
        if (locationJson != null) {
            val gson = Gson()
            return gson.fromJson(locationJson, Location::class.java)
        }
        return updateLocationToDefault()
    }

    fun updateLocationToDefault(): Location {
        val location = Location()
        location.name = DEFAULT_LOCATION_NAME
        location.id = DEFAULT_LOCATION_ID
        location.coordinates = DEFAULT_LOCATION_COORDINATES
        location.locType.name = DEFAULT_LOCATION_CITY

        updateLocation(location)
        return location
    }

    fun updateLocation(location: Location) {
        if (location.coordinates != "0.0, 0.0") {
            val localCacheHandler = LocalCacheHandler(context, TkpdCache.DEALS_LOCATION)
            val gson = Gson()
            val json = gson.toJson(location)
            localCacheHandler.putString(TkpdCache.Key.KEY_DEALS_LOCATION, json)
            localCacheHandler.applyEditor()
        } else updateLocationToDefault()
    }

    fun updateLocationAndCallback(location: Location, callback: CurrentLocationCallback) {
        updateLocation(location)
        callback.setChangedLocation()
    }

    fun detectAndSendLocation(activity: Activity, permissionCheckerHelper: PermissionCheckerHelper,
                              currentLocationCallBack: CurrentLocationCallback) {
        val locationDetectorHelper = LocationDetectorHelper(
                permissionCheckerHelper,
                LocationServices.getFusedLocationProviderClient(activity.applicationContext),
                activity.applicationContext)
        locationDetectorHelper.getLocation(onGetLocation(currentLocationCallBack), activity,
                LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD, "")
    }

    private fun onGetLocation(currentLocationCallBack: CurrentLocationCallback):
            Function1<DeviceLocation?, Unit> {
        return { deviceLocation: DeviceLocation? ->
            if (deviceLocation != null && deviceLocation.latitude != 0.0 && deviceLocation.longitude != 0.0) {
                val coordinate = "${deviceLocation.latitude}, ${deviceLocation.longitude}"
                val location = Location(coordinates = coordinate)
                updateLocation(location)
                currentLocationCallBack.setCurrentLocation(location)
            } else {
                currentLocationCallBack.setCurrentLocation(getLocation())
            }
        }
    }

    companion object {
        const val DEFAULT_LOCATION_NAME = "Jakarta"
        const val DEFAULT_LOCATION_ID = 318
        const val DEFAULT_LOCATION_COORDINATES = "-6.2087634,106.845599"
        const val DEFAULT_LOCATION_CITY = "city"
    }
}
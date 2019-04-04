package com.tokopedia.hotel.destination.view.viewmodel

import android.app.Activity
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.hotel.R
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by jessica on 27/03/19
 */

class HotelDestinationViewModel @Inject constructor(val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher){

    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    fun setPermissionChecker(permissionCheckerHelper: PermissionCheckerHelper) {
        this.permissionCheckerHelper = permissionCheckerHelper
    }

    fun getCurrentLocation(activity: Activity) {
        val locationDetectorHelper = LocationDetectorHelper(
                permissionCheckerHelper,
                LocationServices.getFusedLocationProviderClient(activity),
                activity)
        locationDetectorHelper.getLocation(onGetLocation(), activity,
                LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                activity.getString(R.string.hotel_search_need_permission))
    }

    private fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return { (latitude, longitude, last) ->
            Log.d("OUTPUTTT", latitude.toString() + " " + longitude.toString())
            null
        }
    }
}
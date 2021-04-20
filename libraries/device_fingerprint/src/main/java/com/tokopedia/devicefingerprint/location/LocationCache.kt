package com.tokopedia.devicefingerprint.location

import android.content.Context
import android.location.Location

object LocationCache{
    const val KEY_LOCATION = "KEY_FP_LOCATION"
    const val KEY_LOCATION_LAT = "KEY_FP_LOCATION_LAT"
    const val KEY_LOCATION_LONG = "KEY_FP_LOCATION_LONG"

    var DEFAULT_LATITUDE = -6.175794
    var DEFAULT_LONGITUDE = 106.826457
    fun saveLocation(context: Context, location: Location) {
        val sp = context.getSharedPreferences(KEY_LOCATION, Context.MODE_PRIVATE).edit()
        sp.putString(KEY_LOCATION_LAT, location.latitude.toString())
        sp.putString(KEY_LOCATION_LONG, location.longitude.toString())
        sp.apply()
    }

    fun getLatitudeCache(context: Context):String {
        return context.getSharedPreferences(KEY_LOCATION, Context.MODE_PRIVATE).getString(KEY_LOCATION_LAT, DEFAULT_LATITUDE.toString()) ?: ""
    }

    fun getLongitudeCache(context: Context):String {
        return context.getSharedPreferences(KEY_LOCATION, Context.MODE_PRIVATE).getString(KEY_LOCATION_LONG, DEFAULT_LONGITUDE.toString()) ?: ""
    }
}

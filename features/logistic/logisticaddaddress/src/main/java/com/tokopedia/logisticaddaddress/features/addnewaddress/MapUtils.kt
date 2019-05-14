package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.content.Context
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache

/**
 * Created by fwidjaja on 2019-05-10.
 */

object MapUtils {

    @JvmStatic
    fun generateLatLng(latitude: String, longitude: String): LatLng {
        return generateLatLng(java.lang.Double.parseDouble(latitude), java.lang.Double.parseDouble(longitude))
    }

    @JvmStatic
    fun generateLatLng(latitude: Double?, longitude: Double?): LatLng {
        return latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }!!
    }

    @JvmStatic
    fun saveLocation(context: Context, location: Location) {
        val localCacheHandler = LocalCacheHandler(context, TkpdCache.Key.KEY_LOCATION)
        localCacheHandler.putString(TkpdCache.Key.KEY_LOCATION_LAT, location.latitude.toString())
        localCacheHandler.putString(TkpdCache.Key.KEY_LOCATION_LONG, location.longitude.toString())
        localCacheHandler.applyEditor()
    }
}
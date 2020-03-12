package com.tokopedia.device.info.cache

import android.content.Context
import android.content.SharedPreferences

const val DEVICE_INFO_PREF = "d_info"
const val KEY_IMEI = "im"
const val KEY_IMEI_TIMESTAMP = "im_ts"

class DeviceInfoCache(context: Context) {
    var sp: SharedPreferences = context.getSharedPreferences(DEVICE_INFO_PREF, Context.MODE_PRIVATE)

    /**
     * will return pair of
     * Imei get from cache
     * Boolean true = already cached before
     * Boolean false = never cached before
     */
    fun getImei(): Pair<String, Boolean> {
        val imei = sp.getString(KEY_IMEI, "")
        if (imei.isNullOrEmpty()) {
            val ts = sp.getLong(KEY_IMEI_TIMESTAMP, 0)
            if (ts > 0) {
                return imei to true
            } else {
                return imei to false
            }
        } else {
            return imei to true
        }
    }

    fun setImei(imei: String) {
        sp.edit()
            .putString(KEY_IMEI, imei)
            .putLong(KEY_IMEI_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun clearImei() {
        sp.edit()
            .putString(KEY_IMEI, "")
            .putLong(KEY_IMEI_TIMESTAMP, 0)
            .apply()
    }
}
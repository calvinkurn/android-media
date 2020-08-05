package com.tokopedia.device.info.cache

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

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
                return (imei?:"") to true
            } else {
                return (imei?:"") to false
            }
        } else {
            return imei to true
        }
    }

    fun setImei(imei: String):String {
        val hashImei = if (imei.isEmpty()) {
            ""
        } else {
            getMD5Hash(imei)
        }
        sp.edit()
            .putString(KEY_IMEI, hashImei)
            .putLong(KEY_IMEI_TIMESTAMP, System.currentTimeMillis())
            .apply()
        return hashImei
    }

    fun clearImei() {
        sp.edit()
            .putString(KEY_IMEI, "")
            .putLong(KEY_IMEI_TIMESTAMP, 0)
            .apply()
    }

    protected fun getMD5Hash(raw: String): String {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(raw.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuilder()
            for (message in messageDigest) {
                hexString.append(String.format("%02x", message and 0xff.toByte()))
            }
            hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }
}
package com.tokopedia.kyc_centralized.util

import android.content.SharedPreferences
import javax.inject.Inject

class KycSharedPreference @Inject constructor(
    private val sharedPreferences: SharedPreferences
): KycSharedPreferenceInterface {

    override fun saveByteArrayCache(key: String, data: ByteArray) {
        val cacheString = data.toPreservedString
        sharedPreferences.edit()
            .putString(key, cacheString)
            .apply()
    }

    override fun getByteArrayCache(key: String): ByteArray? {
        val cacheString = sharedPreferences.getString(key, "")
        return cacheString?.toPreservedByteArray
    }

    override fun removeCache(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    private val String.toPreservedByteArray: ByteArray
        get() {
            return this.toByteArray(Charsets.ISO_8859_1)
        }

    private val ByteArray.toPreservedString: String
        get() {
            return String(this, Charsets.ISO_8859_1)
        }
}

interface KycSharedPreferenceInterface {
    fun saveByteArrayCache(key: String, data: ByteArray)
    fun getByteArrayCache(key: String): ByteArray?
    fun removeCache(key: String)
}

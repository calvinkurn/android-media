package com.tokopedia.kyc_centralized.util

import android.content.SharedPreferences
import com.tokopedia.kyc_centralized.common.KYCConstant.LIVENESS_TAG
import timber.log.Timber
import javax.inject.Inject

class KycSharedPreferenceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): KycSharedPreference {

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
        Timber.d("$LIVENESS_TAG: Removing key $key")
        sharedPreferences.edit().remove(key).apply()
    }

    override fun saveStringCache(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    override fun getStringCache(key: String): String {
        return sharedPreferences.getString(key, "").orEmpty()
    }

    override fun removeStringCache(key: String) {
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

interface KycSharedPreference {
    fun saveByteArrayCache(key: String, data: ByteArray)
    fun getByteArrayCache(key: String): ByteArray?
    fun removeCache(key: String)
    fun saveStringCache(key: String, value: String)
    fun getStringCache(key: String): String
    fun removeStringCache(key: String)
}

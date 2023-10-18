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

    override fun saveStringCache(key: String, value: String): Boolean {
        return sharedPreferences.edit()
            .putString(key, value)
            .commit()
    }

    override fun getStringCache(key: String): String {
        return sharedPreferences.getString(key, "").orEmpty()
    }

    override fun removeStringCache(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun saveProjectId(projectId: String): Boolean {
        return saveStringCache(KEY_PROJECT_ID, projectId)
    }

    override fun getProjectId(): String {
        return getStringCache(KEY_PROJECT_ID)
    }

    override fun removeProjectId() {
        removeCache(KEY_PROJECT_ID)
    }

    private val String.toPreservedByteArray: ByteArray
        get() {
            return this.toByteArray(Charsets.ISO_8859_1)
        }

    private val ByteArray.toPreservedString: String
        get() {
            return String(this, Charsets.ISO_8859_1)
        }

    companion object {
        private const val KEY_PROJECT_ID = "projectId"
    }
}

interface KycSharedPreference {
    fun saveByteArrayCache(key: String, data: ByteArray)
    fun getByteArrayCache(key: String): ByteArray?
    fun removeCache(key: String)
    fun saveStringCache(key: String, value: String): Boolean
    fun getStringCache(key: String): String
    fun removeStringCache(key: String)
    fun saveProjectId(projectId: String): Boolean
    fun getProjectId(): String
    fun removeProjectId()
}

package com.tokopedia.sessioncommon.data.fingerprint

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.device.info.DeviceInfo
import javax.inject.Inject

class FingerprintPreferenceManager @Inject constructor(@ApplicationContext val context: Context): FingerprintPreference {

    val name = "android_user_biometric_v2"
    val preference: SharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE)

    private fun removeOldId() {
        val oldKey = "uniqueIdBiometric"
        val hasOldId = preference.contains(oldKey)
        if(hasOldId) {
            preference.edit().remove(oldKey).apply()
        }
    }

    override fun saveUniqueIdIfEmpty(id: String) {
        if(isUniqueIdEmpty()) {
            saveUniqueId(id)
        }
    }

    override fun saveUniqueId(id: String) {
        preference.edit().run {
            putString(PARAM_UNIQUE_ID_BIOMETRIC, id)
            apply()
        }
    }

    override fun getUniqueId(): String {
        removeOldId()
        return preference.getString(PARAM_UNIQUE_ID_BIOMETRIC, "") ?: ""
    }

    override fun removeUniqueId() {
        saveUniqueId("")
    }

    override fun getOrCreateUniqueId(): String {
        val cacheUniqueId = preference.getString(PARAM_UNIQUE_ID_BIOMETRIC, "") ?: ""
        if(cacheUniqueId.isEmpty()) {
            val uniqueId = DeviceInfo.getUUID(context)
            saveUniqueId(uniqueId)
            return uniqueId
        }
        return cacheUniqueId
    }

    override fun isUniqueIdEmpty(): Boolean {
        return preference.getString(PARAM_UNIQUE_ID_BIOMETRIC, "")?.isEmpty() == true
    }

    companion object {
        const val PARAM_UNIQUE_ID_BIOMETRIC = "biometric_id_v2"
    }

}

package com.tokopedia.sessioncommon.data.fingerprint

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject

class FingerprintPreferenceManager @Inject constructor(val context: Context): FingerprintPreference {

    val name = "android_user_biometric"
    val preference: SharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE)

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
        return preference.getString(PARAM_UNIQUE_ID_BIOMETRIC, "") ?: ""
    }

    override fun isUniqueIdEmpty(): Boolean {
        return preference.getString(PARAM_UNIQUE_ID_BIOMETRIC, "")?.isEmpty() == true
    }

    companion object {
        const val PARAM_UNIQUE_ID_BIOMETRIC = "uniqueIdBiometric"
    }

}

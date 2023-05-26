package com.tokopedia.sessioncommon.data.ocl

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class OclPreference @Inject constructor(@ApplicationContext context: Context) {

    private val KEY_OCL_TOKEN = "ocl_data"
    private val OCL_PREF_NAME = "OCL_PREFERENCE"

    private val preference = context.getSharedPreferences(OCL_PREF_NAME, Context.MODE_PRIVATE)

    fun storeToken(token: String) {
        preference.edit().putString(KEY_OCL_TOKEN, token).apply()
    }

    fun getToken(): String {
        return preference.getString(KEY_OCL_TOKEN, "") ?: ""
    }
}

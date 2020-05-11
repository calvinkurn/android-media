package com.tokopedia.dynamicfeatures.service

import android.content.Context
import android.content.SharedPreferences

object DFErrorCache {
    const val SHARED_PREF_ERROR_NAME = "df_err_counter"
    private lateinit var sharedPreferences: SharedPreferences

    private fun getSharedPref(context: Context): SharedPreferences {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.applicationContext.getSharedPreferences(
                SHARED_PREF_ERROR_NAME,
                Context.MODE_PRIVATE
            )
        }
        return sharedPreferences
    }

    fun getErrorCounter(context: Context, moduleName:String): Int {
        try {
            val sp = getSharedPref(context)
            return sp.getInt(moduleName, 0)
        } catch (e: Exception) {
            return 0
        }
    }

    fun addErrorCounter(context: Context, moduleName:String) {
        try {
            val sp = getSharedPref(context)
            val errorCounter = sp.getInt(moduleName, 0)
            sp.edit().putInt(moduleName, errorCounter + 1).apply()
        } catch (e: Exception) {

        }
    }

}
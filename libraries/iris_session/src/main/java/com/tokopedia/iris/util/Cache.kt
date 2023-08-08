package com.tokopedia.iris.util

import android.content.Context

/**
 * Created by meta on 18/04/19.
 */
class Cache(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setEnabled(isEnabled: Boolean) {
        editor.putBoolean(IRIS_ENABLED, isEnabled)
        editor.commit()
    }

    fun isEnabled() : Boolean {
        return sharedPreferences.getBoolean(IRIS_ENABLED, true)
    }

    fun setPerformanceEnabled(isEnabled: Boolean) {
        editor.putBoolean(IRIS_PERFORMANCE_ENABLED, isEnabled)
        editor.commit()
    }

    fun isPerformanceEnabled() : Boolean {
        return sharedPreferences.getBoolean(IRIS_PERFORMANCE_ENABLED, true)
    }
}

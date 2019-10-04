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
        return sharedPreferences.getBoolean(IRIS_ENABLED, false)
    }

    fun setEnableLogEntries(isEnabled: Boolean) {
        editor.putBoolean(IRIS_LOG_ENABLED, isEnabled)
        editor.commit()
    }

    fun isEnableLogEntries(): Boolean {
        return sharedPreferences.getBoolean(IRIS_LOG_ENABLED, false)
    }

    fun setEnableAlarm(isAlarmOn: Boolean) {
        editor.putBoolean(IRIS_IS_ALARM_ON, isAlarmOn)
        editor.commit()
    }

    fun isAlarmOn(): Boolean {
        return sharedPreferences.getBoolean(IRIS_IS_ALARM_ON, false)
    }
}

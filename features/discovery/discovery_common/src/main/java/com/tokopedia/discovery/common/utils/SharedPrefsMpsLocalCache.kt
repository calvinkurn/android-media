package com.tokopedia.discovery.common.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsMpsLocalCache(
    context: Context?
) : MpsLocalCache {

    private val sharedPref: SharedPreferences? by lazy {
        context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun isFirstMpsSuccess(): Boolean {
        val sharedPref = sharedPref ?: return false
        return sharedPref.getBoolean(KEY_FIRST_MPS_SUCCESS, false)
    }

    override fun markFirstMpsSuccess() {
        val sharedPref = sharedPref ?: return
        sharedPref.edit()
            .putBoolean(KEY_FIRST_MPS_SUCCESS, true)
            .putLong(TIME_FIRST_MPS_SUCCESS, createdTimeMils())
            .apply()

    }

    override fun shouldAnimatePlusIcon(): Boolean {
        return !isTimeFirstSuccessAlreadyOneMonth()
    }

    private fun createdTimeMils(): Long {
        return System.currentTimeMillis()
    }

    private fun getTimeFirstSuccess() : Long {
        val defaultTime = createdTimeMils()
        val sharedPref = sharedPref ?: return defaultTime
        return sharedPref.getLong(TIME_FIRST_MPS_SUCCESS, defaultTime)
    }

    private fun isTimeFirstSuccessAlreadyOneMonth(): Boolean {
        val timePassed = createdTimeMils() - getTimeFirstSuccess()
        return timePassed > THIRTY_DAYS
    }

    companion object {
        private const val PREF_NAME = "MPSSharedPref"
        const val KEY_FIRST_MPS_SUCCESS = "KEY_FIRST_MPS_SUCCESS"
        const val TIME_FIRST_MPS_SUCCESS = "TIME_FIRST_MPS_SUCCESS"
        const val THIRTY_DAYS = 2592000000L
    }
}

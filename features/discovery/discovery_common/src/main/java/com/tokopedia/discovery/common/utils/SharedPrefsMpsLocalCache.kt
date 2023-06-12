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
            .apply()

    }

    override fun shouldAnimatePlusIcon(): Boolean {
        return !isFirstMpsSuccess()
    }

    companion object {
        private const val PREF_NAME = "MPSSharedPref"
        const val KEY_FIRST_MPS_SUCCESS = "KEY_FIRST_MPS_SUCCESS"
    }
}

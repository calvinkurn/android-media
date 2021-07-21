package com.tokopedia.selleronboarding.old.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created By @ilhamsuaib on 13/04/20
 */

class OnboardingPreference(private val context: Context) {

    companion object {
        private const val ONBOARDING_PREF = "onboarding_preference"
        @JvmField
        val HAS_OPEN_ONBOARDING = "has_open_onboarding"
    }

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(ONBOARDING_PREF, Context.MODE_PRIVATE)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean = sharedPref.getBoolean(key, defValue)
}
package com.tokopedia.tokopedianow.common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class TokoNowSharedPreference @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val KEY_HOME_SHARED_PREF = "tokonow_shared_pref"
        private const val KEY_HOME_2H_SWITCHER_ONBOARD = "switcher_onboard_2h"
        private const val KEY_HOME_20M_SWITCHER_ONBOARD = "switcher_onboard_20m"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(KEY_HOME_SHARED_PREF, Context.MODE_PRIVATE)
    }

    fun set20mSwitcherOnBoardShown(shown: Boolean) {
        sharedPref.edit().putBoolean(KEY_HOME_20M_SWITCHER_ONBOARD, shown).apply()
    }

    fun get20mSwitcherOnBoardShown(): Boolean {
        return sharedPref.getBoolean(KEY_HOME_20M_SWITCHER_ONBOARD, false)
    }

    fun set2hSwitcherOnBoardShown(shown: Boolean) {
        sharedPref.edit().putBoolean(KEY_HOME_2H_SWITCHER_ONBOARD, shown).apply()
    }

    fun get2hSwitcherOnBoardShown(): Boolean {
        return sharedPref.getBoolean(KEY_HOME_2H_SWITCHER_ONBOARD, false)
    }
}
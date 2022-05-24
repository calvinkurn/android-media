package com.tokopedia.tokopedianow.home.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class HomeSharedPreference @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val KEY_HOME_SHARED_PREF = "tokonow_home_shared_pref"
        private const val KEY_HOME_2H_SWITCHER_COACHMARK = "home_switcher_coachmark_2h"
        private const val KEY_HOME_20M_SWITCHER_COACHMARK = "home_switcher_coachmark_20m"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(KEY_HOME_SHARED_PREF, Context.MODE_PRIVATE)
    }

    fun set20mSwitcherCoachMarkShown(shown: Boolean) {
        sharedPref.edit().putBoolean(KEY_HOME_20M_SWITCHER_COACHMARK, shown).apply()
    }

    fun get20mSwitcherCoachMarkShown(): Boolean {
        return sharedPref.getBoolean(KEY_HOME_20M_SWITCHER_COACHMARK, false)
    }

    fun set2hSwitcherCoachMarkShown(shown: Boolean) {
        sharedPref.edit().putBoolean(KEY_HOME_2H_SWITCHER_COACHMARK, shown).apply()
    }

    fun get2hSwitcherCoachMarkShown(): Boolean {
        return sharedPref.getBoolean(KEY_HOME_2H_SWITCHER_COACHMARK, false)
    }
}
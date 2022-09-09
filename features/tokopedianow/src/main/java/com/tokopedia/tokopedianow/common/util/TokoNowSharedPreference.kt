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
        private const val KEY_HOME_20M_BOTTOMSHEET_ONBOARD = "bottomsheet_onboard_20m"
    }

    private val sharedPref by lazy {
        context.getSharedPreferences(KEY_HOME_SHARED_PREF, Context.MODE_PRIVATE)
    }

    fun set20mCoachMarkOnBoardShown(shown: Boolean) {
        sharedPref.edit().putBoolean(KEY_HOME_20M_SWITCHER_ONBOARD, shown).apply()
    }

    fun get20mCoachMarkOnBoardShown(): Boolean {
        return sharedPref.getBoolean(KEY_HOME_20M_SWITCHER_ONBOARD, false)
    }

    fun set2hCoachMarkOnBoardShown(shown: Boolean) {
        sharedPref.edit().putBoolean(KEY_HOME_2H_SWITCHER_ONBOARD, shown).apply()
    }

    fun get2hCoachMarkOnBoardShown(): Boolean {
        return sharedPref.getBoolean(KEY_HOME_2H_SWITCHER_ONBOARD, false)
    }

    fun set20mBottomSheetOnBoardShown(shown: Boolean) {
        sharedPref.edit().putBoolean(KEY_HOME_20M_BOTTOMSHEET_ONBOARD, shown).apply()
    }

    fun get20mBottomSheetOnBoardShown(): Boolean {
        return sharedPref.getBoolean(KEY_HOME_20M_BOTTOMSHEET_ONBOARD, false)
    }
}
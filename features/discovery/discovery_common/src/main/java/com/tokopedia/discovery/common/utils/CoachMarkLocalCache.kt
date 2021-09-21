package com.tokopedia.discovery.common.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Yehezkiel on 24/02/21
 */
class CoachMarkLocalCache(context: Context?) {

    companion object {
        const val BOE_PREF_NAME = "BoeSharedPref"
        const val KEY_PREF_NAME_PM_PRO = "KEY_PREF_NAME_POWER_MERCHANT_PRO"
        const val KEY_SHOW_COACHMARK_BOE = "KEY_SHOW_COACHMARK_BOE"
        const val KEY_SHOW_COACH_MARK_HOME_POWER_MERCHANT_PRO = "KEY_SHOW_COACH_MARK_HOME_POWER_MERCHANT_PRO"
        const val KEY_SHOW_POP_UP_SEARCH_POWER_MERCHANT_PRO = "KEY_SHOW_POP_UP_SEARCH_POWER_MERCHANT_PRO"
    }

    private val sharedPref: SharedPreferences? by lazy {
        context?.getSharedPreferences(BOE_PREF_NAME, Context.MODE_PRIVATE)
    }

    private val pmProSharedPref: SharedPreferences? by lazy {
        context?.getSharedPreferences(KEY_PREF_NAME_PM_PRO, Context.MODE_PRIVATE)
    }

    fun shouldShowBoeCoachmark(): Boolean {
        val shouldShow = sharedPref?.getBoolean(KEY_SHOW_COACHMARK_BOE, true) ?: false
        if (shouldShow) {
            setShown()
        }
        return shouldShow
    }

    private fun setShown() {
        sharedPref?.edit()?.putBoolean(KEY_SHOW_COACHMARK_BOE, false)?.apply()
    }

    fun shouldShowHomePMProCoachMark(): Boolean {
        val shouldShow = pmProSharedPref?.getBoolean(KEY_SHOW_COACH_MARK_HOME_POWER_MERCHANT_PRO, true) ?: false
        if (shouldShow) {
            setShown(KEY_SHOW_COACH_MARK_HOME_POWER_MERCHANT_PRO)
        }
        return shouldShow
    }

    private fun setShown(key: String) {
        pmProSharedPref?.edit()?.putBoolean(key, false)?.apply()
    }

    fun shouldShowSearchPMProPopUp(): Boolean {
        val shouldShow = pmProSharedPref?.getBoolean(KEY_SHOW_POP_UP_SEARCH_POWER_MERCHANT_PRO, true) ?: false
        if (shouldShow) {
            setShown(KEY_SHOW_POP_UP_SEARCH_POWER_MERCHANT_PRO)
            setShown(KEY_SHOW_COACH_MARK_HOME_POWER_MERCHANT_PRO)
        }
        return shouldShow
    }
}
package com.tokopedia.discovery.common.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Yehezkiel on 24/02/21
 */
class CoachMarkLocalCache(context: Context?) {

    companion object {
        const val BOE_PREF_NAME = "BoeSharedPref"
        const val KEY_SHOW_COACHMARK_BOE = "KEY_SHOW_COACHMARK_BOE"
    }

    private val sharedPref: SharedPreferences? by lazy {
        context?.getSharedPreferences(BOE_PREF_NAME, Context.MODE_PRIVATE)
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
}
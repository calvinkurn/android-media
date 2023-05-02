package com.tokopedia.purchase_platform.common.prefs

import android.content.Context
import android.content.SharedPreferences

class PlusCoachmarkPrefs(val context: Context) {
    companion object {
        const val PLUS_COACHMARK_HAS_SHOWN = "plus_coachmark_has_shown"
        const val PURCHASE_PLATFORM_PLUS_COACHMARK = "purchase_platform_plus_coachmark"
    }

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs = context.getSharedPreferences(PURCHASE_PLATFORM_PLUS_COACHMARK, Context.MODE_PRIVATE)
    }

    fun setPlusCoachmarkHasShown(isShown: Boolean) {
        sharedPrefs?.edit()?.putBoolean(PLUS_COACHMARK_HAS_SHOWN, isShown)?.apply()
    }

    fun getPlusCoachMarkHasShown(): Boolean {
        return sharedPrefs?.getBoolean(PLUS_COACHMARK_HAS_SHOWN, false) ?: false
    }
}

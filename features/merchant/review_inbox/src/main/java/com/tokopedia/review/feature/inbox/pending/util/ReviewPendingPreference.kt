package com.tokopedia.review.feature.inbox.pending.util

import android.content.Context
import android.content.SharedPreferences

class ReviewPendingPreference(context: Context?) {

    private var sharedPrefs: SharedPreferences? = null

    companion object {
        private const val DISCUSSION_PREF = "review_pending.pref"
        const val KEY_SHOW_COACH_MARK = "showCoachMark"
        const val COACH_MARK_INITIAL_VALUE = true
    }

    init {
        this.sharedPrefs = context?.getSharedPreferences(DISCUSSION_PREF, Context.MODE_PRIVATE)
    }

    fun updateSharedPrefs(flag: Boolean) {
        sharedPrefs?.run {
            edit().putBoolean(KEY_SHOW_COACH_MARK, flag).apply()
        }
    }

    fun isShowCoachMark(): Boolean {
        return sharedPrefs?.getBoolean(KEY_SHOW_COACH_MARK, COACH_MARK_INITIAL_VALUE) ?: false
    }
}
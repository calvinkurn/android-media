package com.tokopedia.createpost.view.util

import android.content.Context

object CreatePostPrefs {
    private const val PREF_KEY = "create_post_feed"

    private const val SHOULD_SHOW_COACHMARK = "should_show_coach_mark"


    fun saveShouldShowCoachMarkValue(context: Context) {
        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sp.edit()
            .putBoolean(SHOULD_SHOW_COACHMARK, false)
            .apply()
    }

    fun getShouldShowCoachMarkValue(context: Context): Boolean {
        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return sp.getBoolean(SHOULD_SHOW_COACHMARK, true)
    }
}


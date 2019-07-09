package com.tokopedia.twitter_share.session

import android.content.Context
import android.content.SharedPreferences

object TwitterPreference {

    private const val PREFS = "TWITTER_SESSION_PREFS"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }
}
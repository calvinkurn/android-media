package com.tokopedia.kelontongapp.firebase

import android.content.Context
import android.content.SharedPreferences

import android.content.Context.MODE_PRIVATE
import com.tokopedia.kelontongapp.*

/**
 * Created by meta on 16/10/18.
 */
object Preference {

    private fun sharedPrefences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_MITRA_APPLICATION, MODE_PRIVATE)
    }

    fun saveFcmToken(context: Context, token: String) {
        val editor = sharedPrefences(context).edit()
        editor.putString(PREFERENCES_FIREBASE_TOKEN, token)
        editor.apply()
    }

    fun getFcmToken(context: Context): String {
        val preferences = sharedPrefences(context)
        return preferences.getString(PREFERENCES_FIREBASE_TOKEN, "")
    }

    fun saveFirstTime(context: Context) {
        val editor = sharedPrefences(context).edit()
        editor.putString(PREFERENCES_FIRST_TIME, "1")
        editor.apply()
    }

    fun isFirstTime(context: Context): Boolean {
        val preferences = sharedPrefences(context)
        val isUserFirstTime = preferences.getString(PREFERENCES_FIRST_TIME, "")
        return isUserFirstTime!!.isEmpty()
    }
}

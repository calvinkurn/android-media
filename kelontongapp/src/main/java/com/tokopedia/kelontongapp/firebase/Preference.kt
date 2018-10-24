package com.tokopedia.kelontongapp.firebase

import android.content.Context
import android.content.SharedPreferences

import com.tokopedia.kelontongapp.KelontongMainActivity
import com.tokopedia.kelontongapp.KelontongMainApplication

import android.content.Context.MODE_PRIVATE

/**
 * Created by meta on 16/10/18.
 */
object Preference {

    private fun sharedPrefences(context: Context): SharedPreferences {
        return context.getSharedPreferences(KelontongMainApplication::class.java!!.getName(), MODE_PRIVATE)
    }

    fun saveFcmToken(context: Context, token: String) {
        val editor = sharedPrefences(context).edit()
        editor.putString(FirebaseIDService::class.java!!.getName(), token)
        editor.apply()
    }

    fun getFcmToken(context: Context): String {
        val preferences = sharedPrefences(context)
        return preferences.getString(FirebaseIDService::class.java!!.getName(), "")
    }

    fun saveFirstTime(context: Context) {
        val editor = sharedPrefences(context).edit()
        editor.putString(KelontongMainActivity::class.java!!.getName(), "1")
        editor.apply()
    }

    fun isFirstTime(context: Context): Boolean {
        val preferences = sharedPrefences(context)
        val isUserFirstTime = preferences.getString(KelontongMainActivity::class.java!!.getName(), "")
        return isUserFirstTime!!.isEmpty()
    }
}

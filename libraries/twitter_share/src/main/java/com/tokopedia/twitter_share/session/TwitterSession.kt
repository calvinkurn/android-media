package com.tokopedia.twitter_share.session

import android.content.SharedPreferences

class TwitterSession(
    private val sharedPref: SharedPreferences
) {

    companion object {
        private const val ACCESS_TOKEN = "twitter_access_token"
        private const val ACCESS_TOKEN_SECRET = "twitter_access_token_secret"
    }

    fun getAccessToken(): String? {
        return sharedPref.getString(ACCESS_TOKEN, null)
    }

    fun getAccessTokenSecret(): String? {
        return sharedPref.getString(ACCESS_TOKEN_SECRET, null)
    }

    fun setAccessTokenAndSecret(token: String, tokenSecret: String) {
        sharedPref.edit {
            putString(ACCESS_TOKEN, token)
            putString(ACCESS_TOKEN_SECRET, tokenSecret)
        }
    }

    private fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        editor.action()
        editor.apply()
    }
}
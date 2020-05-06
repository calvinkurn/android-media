package com.tokopedia.loginfingerprint.data.preference

import android.content.Context

/**
 * Created by Yoris Prayogo on 2020-02-07.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class FingerprintPreferenceHelper(context: Context): FingerprintSetting {

    companion object {
        const val LOGIN_FINGERPRINT_STATUS_KEY = "loginFingerprintStatusKey"
        const val LOGIN_FINGERPRINT_USER_ID_KEY = "loginFingerprintUserIdKey"
    }

    private val prefName = "LoginFingerprintPrefs"
    private var prefs = context.getSharedPreferences(
            prefName, Context.MODE_PRIVATE)

    override fun registerFingerprint(){
        prefs.edit().putBoolean(LOGIN_FINGERPRINT_STATUS_KEY, true).apply()
    }

    override fun saveUserId(userId: String){
        prefs.edit().putString(LOGIN_FINGERPRINT_USER_ID_KEY, userId).apply()
    }

    override fun removeUserId(){
        prefs.edit().putString(LOGIN_FINGERPRINT_USER_ID_KEY, "0").apply()
    }

    override fun getFingerprintUserId(): String = prefs.getString(LOGIN_FINGERPRINT_USER_ID_KEY, "0") ?: "0"

    override fun unregisterFingerprint(){
        prefs.edit().putBoolean(LOGIN_FINGERPRINT_STATUS_KEY, false).apply()
    }

    override fun isFingerprintRegistered(): Boolean = prefs.getBoolean(LOGIN_FINGERPRINT_STATUS_KEY, false)
}
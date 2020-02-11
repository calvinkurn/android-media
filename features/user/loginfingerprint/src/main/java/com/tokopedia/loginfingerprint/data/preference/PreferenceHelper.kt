package com.tokopedia.loginfingerprint.data.preference

import android.content.Context

/**
 * Created by Yoris Prayogo on 2020-02-07.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class PreferenceHelper(context: Context) {

    companion object {
        const val LOGIN_FINGERPRINT_STATUS_KEY = "loginFingerprintStatusKey"
    }

    private val prefName = "LoginFingerprintPrefs"
    private var prefs = context.getSharedPreferences(
            prefName, Context.MODE_PRIVATE)

    fun registerFingerprint(){
        prefs.edit().putBoolean(LOGIN_FINGERPRINT_STATUS_KEY, true).apply()
    }

    fun isFingerprintRegistered(): Boolean = prefs.contains(LOGIN_FINGERPRINT_STATUS_KEY)
}
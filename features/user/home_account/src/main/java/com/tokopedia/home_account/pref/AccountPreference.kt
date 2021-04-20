package com.tokopedia.home_account.pref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.home_account.AccountConstants
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class AccountPreference @Inject constructor(val context: Context) {

    var preference: SharedPreferences? = null

    init {
        preference = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun isItemSelected(key: String, defaultValue: Boolean): Boolean {
        return preference?.getBoolean(key, defaultValue) ?: defaultValue
    }

    fun saveSettingValue(key: String, isChecked: Boolean) {
        val editor = preference?.edit()
        editor?.putBoolean(key, isChecked)
        editor?.apply()
    }

    fun isShowCoachmark(): Boolean {
        return preference?.getBoolean(AccountConstants.KEY.KEY_SHOW_COACHMARK, true) ?: true
    }

    fun getSafeMode(): Boolean {
        return preference?.getBoolean(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, false) ?: false
    }
}
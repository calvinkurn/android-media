package com.tokopedia.loginregister.external_register.base.data

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants.PREF_KEY_AUTH_CODE
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants.PREF_KEY_GOAL_ID
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants.PREF_KEY_NAME
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants.PREF_KEY_NEED_CONTINUE
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants.PREF_KEY_PHONE
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 05/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class ExternalRegisterPreference @Inject constructor(private var context: Context) {

    private var sharedPreference: SharedPreferences? = null

    val SHARED_PREFERENCE = "externalRegisterSharedPreference"

    init {
        sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    fun saveName(name: String){
        sharedPreference?.edit()?.putString(PREF_KEY_NAME, name)?.apply()
    }

    fun savePhone(name: String){
        sharedPreference?.edit()?.putString(PREF_KEY_PHONE, name)?.apply()
    }

    fun saveGoalKey(goalKey: String){
        sharedPreference?.edit()?.putString(PREF_KEY_GOAL_ID, goalKey)?.apply()
    }

    fun saveAuthCode(authCode: String){
        sharedPreference?.edit()?.putString(PREF_KEY_AUTH_CODE, authCode)?.apply()
    }

    fun isNeedContinue(needContinue: Boolean){
        sharedPreference?.edit()?.putBoolean(PREF_KEY_NEED_CONTINUE, needContinue)?.apply()
    }

    fun getGoalKey(): String = sharedPreference?.getString(PREF_KEY_GOAL_ID, "") ?: ""
    fun getAuthCode(): String = sharedPreference?.getString(PREF_KEY_AUTH_CODE, "") ?: ""
    fun getName(): String = sharedPreference?.getString(PREF_KEY_NAME, "") ?: ""
    fun getPhone(): String = sharedPreference?.getString(PREF_KEY_PHONE, "") ?: ""
    fun isNeedContinue(): Boolean = sharedPreference?.getBoolean(PREF_KEY_NEED_CONTINUE, false) ?: false
}
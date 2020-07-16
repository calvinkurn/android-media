package com.tokopedia.additional_check.data.pref

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 08/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class AdditionalCheckPreference @Inject constructor(val context: Context) {

    private val USER_ADDITIONAL_CHECK = "user_additional_check"
    private val USER_ADDITIONAL_CHECK_INTERVAL_KEY = "user_additional_check"

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs = context.getSharedPreferences(
                USER_ADDITIONAL_CHECK,
                Context.MODE_PRIVATE)
    }

    fun setInterval(interval: Int){
        val days = interval * 24 * 60 * 60 * 1000
        val nextCheck = days + System.currentTimeMillis()
        sharedPrefs?.edit()?.putLong(USER_ADDITIONAL_CHECK_INTERVAL_KEY, nextCheck)?.apply()
    }

    fun isNeedCheck(): Boolean {
//        return true
        val interval = sharedPrefs?.getLong(USER_ADDITIONAL_CHECK_INTERVAL_KEY, 0)
        return System.currentTimeMillis() > interval ?: 0
    }

}
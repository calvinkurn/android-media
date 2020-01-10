package com.tokopedia.notifcenter.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Ade Fulki on 2019-09-09.
 * ade.hadian@tokopedia.com
 */

class NotifPreference(val context: Context){

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(NOTIF_PREFERENCE, Context.MODE_PRIVATE)
    }

    var isDisplayedGimmickNotif: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_DISPLAYED_GIMMICK_NOTIF, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_IS_DISPLAYED_GIMMICK_NOTIF, value).apply()

    companion object {
        private const val NOTIF_PREFERENCE = "notif_preference"
        private const val KEY_IS_DISPLAYED_GIMMICK_NOTIF = "key_is_displayed_gimmick_notif"
    }
}
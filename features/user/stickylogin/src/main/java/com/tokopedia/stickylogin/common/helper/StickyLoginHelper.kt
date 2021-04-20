package com.tokopedia.stickylogin.common.helper

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.stickylogin.common.StickyLoginConstant


fun getPrefStickyLogin(context: Context): SharedPreferences {
    return context.getSharedPreferences(StickyLoginConstant.STICKY_PREF, Context.MODE_PRIVATE)
}

fun getPrefLoginReminder(context: Context): SharedPreferences {
    return context.getSharedPreferences(StickyLoginConstant.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
}

fun saveIsRegisteredFromStickyLogin(context: Context, state: Boolean) {
    getPrefStickyLogin(context).run {
        edit().putBoolean(StickyLoginConstant.KEY_IS_REGISTER_FROM_STICKY_LOGIN, state).apply()
    }
}

fun isRegisteredFromStickyLogin(context: Context): Boolean {
    return context.getSharedPreferences(StickyLoginConstant.STICKY_PREF, Context.MODE_PRIVATE).getBoolean(StickyLoginConstant.KEY_IS_REGISTER_FROM_STICKY_LOGIN, false)
}
package com.tokopedia.usercomponents.stickylogin.common.helper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.sessioncommon.data.ocl.OclPreference
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant


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

fun getLoginIntentPage(context: Context): Intent {
    val preference = OclPreference(context)
    return if(preference.getToken().isNotEmpty()) {
        RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.CHOOSE_ACCOUNT_OCL)
    } else {
        RouteManager.getIntent(context, ApplinkConst.LOGIN)
    }
}

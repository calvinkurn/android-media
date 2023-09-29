package com.scp.auth.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform

internal fun goToForgotPassword(context: Context) {
    val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.FORGOT_PASSWORD)
    intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
    context.startActivity(intent)
}

internal fun goToChangePIN(context: Context) {
    val bundle = Bundle()
    val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.CHANGE_PIN).apply {
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN, true)
        putExtras(bundle)
    }
    intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
    context.startActivity(intent)
}

internal fun goToInactivePhoneNumber(context: Context) {
    val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INPUT_OLD_PHONE_NUMBER)
    context.startActivity(intent)
}

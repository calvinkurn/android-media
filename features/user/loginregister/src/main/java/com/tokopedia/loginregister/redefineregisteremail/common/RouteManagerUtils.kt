package com.tokopedia.loginregister.redefineregisteremail.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.redefineregisteremail.common.routedataparam.GoToVerificationRegisterParam
import com.tokopedia.loginregister.redefineregisteremail.common.routedataparam.GoToVerificationUpdateParam

fun Context.intentGoToVerificationRegister(
    param: GoToVerificationRegisterParam
): Intent {
    val intent = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.COTP)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, param.phone)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, param.email)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, param.source)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, param.otpType)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, param.otpMode)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN, param.token)
    return intent
}

fun Context.intentGoToVerificationUpdatePhone(
    param: GoToVerificationUpdateParam
): Intent {
    val intent = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.COTP)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, param.phone)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, param.source)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, param.otpType)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, param.otpMode)
    return intent
}

fun Context.intentGoToHome(): Intent {
    val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    return intent
}

fun Context.intentGoToLoginWithEmail(email: String, source: String): Intent {
    val intent = RouteManager.getIntent(
        this,
        ApplinkConstInternalUserPlatform.LOGIN_EMAIL,
        Uri.encode(email),
        source
    )
    intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, true)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, true)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    return intent
}

fun Context.intentGoToLoginWithPhone(phone: String, source: String): Intent {
    val intent = RouteManager.getIntent(
        this,
        ApplinkConstInternalUserPlatform.LOGIN_PHONE,
        phone,
        source
    )
    intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, true)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, true)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    return intent
}
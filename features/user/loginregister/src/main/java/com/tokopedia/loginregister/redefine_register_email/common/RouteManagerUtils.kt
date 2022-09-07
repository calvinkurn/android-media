package com.tokopedia.loginregister.redefine_register_email.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform

fun intentGoToVerification(
    phone: String = "",
    email: String = "",
    otpType: Int,
    source: String = "",
    token: String = "",
    context: Context
): Intent {
    val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN, token)
    return intent
}

fun intentGoToInputPhone(
    email: String,
    encryptedPassword: String,
    name: String,
    source: String,
    isRequiredInputPhone: Boolean,
    token: String,
    hash: String,
    context: Context
): Intent {
    val intent = RouteManager.getIntent(
        context,
        ApplinkConstInternalUserPlatform.REDEFINE_REGISTER_INPUT_PHONE
    )
    intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_VALUE_EMAIL, email)
    intent.putExtra(
        ApplinkConstInternalUserPlatform.PARAM_VALUE_ENCRYPTED_PASSWORD,
        encryptedPassword
    )
    intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_VALUE_NAME, name)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
    intent.putExtra(
        ApplinkConstInternalUserPlatform.PARAM_IS_REGISTER_REQUIRED_INPUT_PHONE,
        isRequiredInputPhone
    )
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, token)
    intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_HASH, hash)
    return intent
}

fun intentGoToHome(context: Context): Intent {
    val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    return intent
}

fun intentGoToLoginWithEmail(email: String, source: String, context: Context): Intent {
    val intent = RouteManager.getIntent(
        context,
        ApplinkConstInternalUserPlatform.LOGIN_EMAIL,
        Uri.encode(email),
        source
    )
    intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, true)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, true)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    return intent
}

fun intentGoToLoginWithPhone(phone: String, source: String, context: Context): Intent {
    val intent = RouteManager.getIntent(
        context,
        ApplinkConstInternalUserPlatform.LOGIN_PHONE,
        phone,
        source
    )
    intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, true)
    intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, true)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    return intent
}
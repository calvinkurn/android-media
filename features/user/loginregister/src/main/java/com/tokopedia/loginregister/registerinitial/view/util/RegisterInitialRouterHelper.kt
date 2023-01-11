package com.tokopedia.loginregister.registerinitial.view.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterEmailActivity
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 16/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RegisterInitialRouterHelper @Inject constructor() {

    var source = ""

    fun goToVerification(phone: String = "", email: String = "", otpType: Int, context: Context): Intent {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        return intent
    }

    fun goToLoginPage(context: Activity){
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        context.startActivity(intent)
        context.finish()
    }

    fun goToRegisterEmail(fragment: Fragment){
        val intent = RegisterEmailActivity.getCallingIntent(fragment.context)
        fragment.startActivityForResult(intent, RegisterConstants.Request.REQUEST_REGISTER_EMAIL)
    }

    fun goToRegisterEmailPageWithParams(fragment: Fragment, email: String, token: String, source: String){
        val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalUserPlatform.EMAIL_REGISTER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, token)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        fragment.startActivityForResult(intent, RegisterConstants.Request.REQUEST_REGISTER_EMAIL)
    }

    fun goToRedefineRegisterEmailPageWithParams(fragment: Fragment, source: String, isRequiredInputPhone: Boolean){
        val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalUserPlatform.REDEFINE_REGISTER_EMAIL)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_REGISTER_REQUIRED_INPUT_PHONE, isRequiredInputPhone)
        fragment.startActivity(intent)
    }

    fun goToChooseAccountPage(fragment: Fragment, accessToken: String, phoneNumber: String){
        val intent = RouteManager.getIntent(fragment.context,
                ApplinkConstInternalUserPlatform.CHOOSE_ACCOUNT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, true)

        fragment.startActivityForResult(intent, RegisterConstants.Request.REQUEST_CHOOSE_ACCOUNT)
    }

    fun goToChangeName(fragment: Fragment, validateToken: String) {
        val intent = (fragment.context?.applicationContext as ApplinkRouter).getApplinkIntent(fragment.context, ApplinkConst.ADD_NAME_PROFILE)
        intent.putExtras(Bundle().apply {
            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
        })
        fragment.startActivityForResult(intent, RegisterConstants.Request.REQUEST_CHANGE_NAME)
    }

    fun goToAddPin2FA(fragment: Fragment, enableSkip2FA: Boolean, validateToken: String = ""){
        val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalUserPlatform.ADD_PIN)
        intent.putExtras(Bundle().apply {
            putBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, enableSkip2FA)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
        })
        fragment.startActivityForResult(intent, RegisterConstants.Request.REQUEST_ADD_PIN)
    }

    fun goToAddName(fragment: Fragment, uuid: String, phoneNumber: String){
        val applink = ApplinkConstInternalUserPlatform.ADD_NAME_REGISTER
        val intent = RouteManager.getIntent(fragment.context, applink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, uuid)
        fragment.startActivityForResult(intent, RegisterConstants.Request.REQUEST_ADD_NAME_REGISTER_PHONE)
    }
}

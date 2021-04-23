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
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterEmailActivity
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment

/**
 * Created by Yoris Prayogo on 16/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RegisterInitialRouterHelper {

    var source = ""

    fun goToVerification(phone: String = "", email: String = "", otpType: Int, context: Context): Intent {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
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
        fragment.startActivityForResult(intent, RegisterInitialFragment.REQUEST_REGISTER_EMAIL)
    }

    fun goToRegisterEmailPageWithParams(fragment: Fragment, email: String, token: String, source: String){
        val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalGlobal.EMAIL_REGISTER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, token)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        fragment.startActivityForResult(intent, RegisterInitialFragment.REQUEST_REGISTER_EMAIL)
    }

    fun goToChooseAccountPage(fragment: Fragment, accessToken: String, phoneNumber: String){
        val intent = RouteManager.getIntent(fragment.context,
                ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, true)

        fragment.startActivityForResult(intent, RegisterInitialFragment.REQUEST_CHOOSE_ACCOUNT)
    }

    fun goToChooseAccountPageFacebook(fragment: Fragment, accessToken: String){
        val intent = RouteManager.getIntent(fragment.context,
                ApplinkConstInternalGlobal.CHOOSE_ACCOUNT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, RegisterInitialFragment.FACEBOOK_LOGIN_TYPE)

        fragment.startActivityForResult(intent, RegisterInitialFragment.REQUEST_CHOOSE_ACCOUNT)
    }

    fun goToChangeName(fragment: Fragment) {
        val intent = (fragment.context?.applicationContext as ApplinkRouter).getApplinkIntent(fragment.context, ApplinkConst.ADD_NAME_PROFILE)
        fragment.startActivityForResult(intent, RegisterInitialFragment.REQUEST_CHANGE_NAME)
    }

    fun goToAddPin2FA(fragment: Fragment, enableSkip2FA: Boolean, validateToken: String = ""){
        val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalGlobal.ADD_PIN)
        intent.putExtras(Bundle().apply {
            putBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, enableSkip2FA)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
        })
        fragment.startActivityForResult(intent, RegisterInitialFragment.REQUEST_ADD_PIN)
    }

    fun goToAddName(fragment: Fragment, uuid: String, phoneNumber: String){
        val applink = ApplinkConstInternalGlobal.ADD_NAME_REGISTER_CLEAN_VIEW
        val intent = RouteManager.getIntent(fragment.context, applink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, uuid)
        fragment.startActivityForResult(intent, RegisterInitialFragment.REQUEST_ADD_NAME_REGISTER_PHONE)
    }
}
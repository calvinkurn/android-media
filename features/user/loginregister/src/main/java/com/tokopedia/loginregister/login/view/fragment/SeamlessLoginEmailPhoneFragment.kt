package com.tokopedia.loginregister.login.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import kotlinx.android.synthetic.main.fragment_login_with_phone.view.*

/**
 * Created by Yoris Prayogo on 29/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SeamlessLoginEmailPhoneFragment: LoginEmailPhoneFragment() {
    companion object {
        private const val REMOTE_CONFIG_SEAMLESS_LOGIN = "android_user_seamless_login"
        const val REQUEST_SEAMLESS_LOGIN = 122

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = SeamlessLoginEmailPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun isEnableSeamlessLogin(): Boolean {
        context?.run {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(this)
            return firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_SEAMLESS_LOGIN, false)
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isEnableSmartLock = false
        isAutoLogin = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(isEnableSeamlessLogin() && GlobalConfig.isSellerApp()) {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalSellerapp.SEAMLESS_CHOOSE_ACCOUNT)
            arguments?.run { intent.putExtras(this) }
            startActivityForResult(intent, REQUEST_SEAMLESS_LOGIN)
        }else {
            RouteManager.route(context, ApplinkConst.LOGIN)
            activity?.finish()
        }

        super.onViewCreated(view, savedInstanceState)
        showFullProgressBar()
    }

    private fun showFullProgressBar(){
        view?.container?.hide()
        view?.progress_bar?.show()
    }

    private fun hideFullProgressBar(){
        view?.container?.show()
        view?.progress_bar?.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_SEAMLESS_LOGIN) {
            if(resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    if(data.extras != null) {
                        if (data.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK, false) == true) {
                            onGoToSecurityQuestion("").invoke()
                        } else {
                            presenter.getUserInfo()
                        }
                    }
                } else {
                    presenter.getUserInfo()
                }
            }
        }else if (requestCode == REQUEST_SECURITY_QUESTION
                && resultCode == Activity.RESULT_OK
                && data != null) {
            data.extras?.let {
                val validateToken = it.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                presenter.reloginAfterSQ(validateToken)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
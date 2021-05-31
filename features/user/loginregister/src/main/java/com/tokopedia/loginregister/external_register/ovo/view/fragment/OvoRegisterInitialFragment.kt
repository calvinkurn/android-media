package com.tokopedia.loginregister.external_register.ovo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.fragment.ExternalRegisterInitialFragment
import com.tokopedia.loginregister.external_register.base.viewmodel.ExternalRegisterViewModel
import com.tokopedia.loginregister.external_register.ovo.view.activity.OvoFinalPageActivity
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Yoris Prayogo on 01/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class OvoRegisterInitialFragment: ExternalRegisterInitialFragment() {

    @Inject
    lateinit var externalRegisterPreference: ExternalRegisterPreference

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    private var enableSkip2Fa = false

    private val externalRegisterViewModel by lazy {
        viewModelProvider.get(ExternalRegisterViewModel::class.java)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ExternalRegisterComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        val args = arguments?.getString(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY) ?: ""
        if(::externalRegisterPreference.isInitialized && externalRegisterPreference.getGoalKey().isNotEmpty() && args.isNotEmpty()) {
            val authCode = getAuthCodeFromUrl(args)
            if(authCode.isNotEmpty()) {
                externalRegisterViewModel.register(authCode)
            } else {
                goToErrorPage()
            }
        }
        else {
            goToErrorPage()
        }
    }

    private fun initObserver(){
        externalRegisterViewModel.registerRequestResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success -> onSuccessOvoRegister(it.data.register)
                is Fail -> goToErrorPage()
            }
        })
        externalRegisterViewModel.getUserInfoResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetUserInfo()
                is Fail -> onFailedGetUserInfo(it.throwable)
            }
        })
    }

    private fun onFailedGetUserInfo(throwable: Throwable) {
        goToErrorPage()
    }

    private fun onSuccessGetUserInfo() {
        onSuccessRegister()
        goToSuccessPage()
    }

    fun goToErrorPage(){
        externalRegisterPreference.isNeedContinue(true)
        val intent = OvoFinalPageActivity.createIntentError(activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        startActivity(intent)
        activity?.finish()
    }

    fun goToSuccessPage(){
        onSuccessRegister()
        val intent = OvoFinalPageActivity.createIntentSuccess(activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

    fun onSuccessOvoRegister(registerRequestDataResult: RegisterInfo){
        userSession.clearToken()
        userSession.setToken(registerRequestDataResult.accessToken, "Bearer", EncoderDecoder.Encrypt(registerRequestDataResult.refreshToken, userSession.refreshTokenIV))

        if (registerRequestDataResult.enable2Fa) {
            enableSkip2Fa = registerRequestDataResult.enableSkip2Fa
            goToAddPin2FA(registerRequestDataResult.enableSkip2Fa)
        } else {
            externalRegisterViewModel.getUserInfo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RegisterInitialFragment.REQUEST_ADD_PIN){
            externalRegisterViewModel.getUserInfo()
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        fun createInstance(bundle: Bundle? = null): OvoRegisterInitialFragment {
            val fragment = OvoRegisterInitialFragment().apply {
                bundle?.run {
                    arguments = this
                }
            }
            return fragment
        }
    }
}
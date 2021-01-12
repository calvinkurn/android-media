package com.tokopedia.loginregister.external_register.ovo.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.viewmodel.ExternalRegisterViewModel
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Yoris Prayogo on 01/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class OvoRegisterInitialFragment: BaseDaggerFragment() {

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

    private val externalRegisterViewModel by lazy {
        viewModelProvider.get(ExternalRegisterViewModel::class.java)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ExternalRegisterComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_external_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        val args = arguments?.getString(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY) ?: ""
        if(::externalRegisterPreference.isInitialized
                && externalRegisterPreference.getGoalKey().isNotEmpty()
                && args.isNotEmpty()) {
                    getAuthCodeFromUrl(args).run {
                        externalRegisterViewModel.register(this)
                    }
        }
        else {
//            activity?.finish()
        }
    }

    fun getAuthCodeFromUrl(url: String): String {
        return try {
            val uri = Uri.parse(URLDecoder.decode(url, Charsets.UTF_8.name()))
            val response = uri.getQueryParameter("response") ?: ""
            val parser = JsonParser()
            parser.parse(response).getAsJsonObject().get("authCode").asString
        } catch (e: Exception) {
            ""
        }
    }
    private fun initObserver(){
        externalRegisterViewModel.registerRequestResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success -> onSuccessOvoRegister(it.data.register)
                is Fail -> onFailedOvoRegister()
            }
        })
    }

    private fun goToAddPin2FA(enableSkip2FA: Boolean) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN)
        intent.putExtras(Bundle().apply {
            putBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, enableSkip2FA)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_2FA, true)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
        })
        startActivityForResult(intent, RegisterInitialFragment.REQUEST_ADD_PIN)
    }

    fun goToSuccessPage(){
        val intent = OvoFinalPageActivity.createIntentSuccess(activity)
        startActivity(intent)
        activity?.finish()
    }

    fun onSuccessOvoRegister(registerRequestDataResult: RegisterInfo){
        userSession.clearToken()
        userSession.setToken(registerRequestDataResult.accessToken, "Bearer", registerRequestDataResult.refreshToken)

        if (registerRequestDataResult.enable2Fa) {
            goToAddPin2FA(registerRequestDataResult.enableSkip2Fa)
        } else {
            goToSuccessPage()
        }

//            else if (registerRequestDataResult is Fail) {
//            val throwable = (registerRequestDataResult as Fail).throwable
//            dismissLoadingProgress()
//            if (throwable is MessageErrorException
//                    && throwable.message != null && throwable.message!!.contains(RegisterEmailFragment.ALREADY_REGISTERED)) {
//                showInfo()
//            } else if (throwable is MessageErrorException
//                    && throwable.message != null) {
//                onErrorRegister(throwable.message)
//            } else {
//                if (context != null) {
//                    val forbiddenMessage = context!!.getString(
//                            com.tokopedia.sessioncommon.R.string.default_request_error_forbidden_auth)
//                    val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
//                    if (errorMessage == forbiddenMessage) {
//                        onForbidden()
//                    } else {
//                        onErrorRegister(errorMessage)
//                    }
//                }
//            }
//        }
//        val intent = OvoFinalPageActivity.createIntentSuccess(activity)
//        startActivity(intent)
//        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RegisterInitialFragment.REQUEST_ADD_PIN && resultCode == Activity.RESULT_OK){
            goToSuccessPage()
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    fun onFailedOvoRegister(){
        val intent = OvoFinalPageActivity.createIntentError(activity)
        startActivity(intent)
        activity?.finish()
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
package com.tokopedia.loginregister.external_register.ovo.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.external_register.base.activity.ExternalRegisterWebViewActivity
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.fragment.BaseAddNameFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseAddNameListener
import com.tokopedia.loginregister.external_register.ovo.data.ActivateOvoData
import com.tokopedia.loginregister.external_register.ovo.view.activity.OvoFinalPageActivity
import com.tokopedia.loginregister.external_register.ovo.viewmodel.OvoAddNameViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 16/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoAddNameFragment: BaseAddNameFragment(), BaseAddNameListener {

    var mPhone = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }

    override fun initInjector() {
        getComponent(ExternalRegisterComponent::class.java).inject(this)
    }

    private val addNameViewModel by lazy {
        viewModelProvider.get(OvoAddNameViewModel::class.java)
    }

    override fun onNextButtonClicked() {
        if(mPhone.isNotEmpty()){
            addNameViewModel.activateOvo(name = getInputText(), phoneNumber = mPhone)
            startButtonLoading()
        } else {
            activity?.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseAddNameListener = this
        mPhone = arguments?.getString(ApplinkConstInternalGlobal.PARAM_PHONE, "") ?: ""
    }

    override fun initObserver() {
        addNameViewModel.activateOvoResponse.observe(this, Observer {
            stopButtonLoading()
            when(it){
                is Success -> onSuccessActivateOvo(it.data.activateOvoData)
                is Fail -> onErrorActivateOvo(it.throwable)
            }
        })
    }

    fun goToExternalWebView(url: String){
        activity?.let {
            val intent = Intent(it, ExternalRegisterWebViewActivity::class.java).apply {
                putExtra(ExternalRegisterConstants.PARAM.URL, url)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            startActivity(intent)
        }
    }

    fun onSuccessActivateOvo(data: ActivateOvoData){
        goToExternalWebView("${data.activationUrl}?k=${data.goalKey}")
        activity?.finish()
    }

    fun onErrorActivateOvo(error: Throwable) {
        goToErrorPage()
    }

    fun goToErrorPage(){
        val intent = OvoFinalPageActivity.createIntentError(activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        startActivity(intent)
        activity?.finish()
    }

    companion object {
        fun createInstance(bundle: Bundle? = null): Fragment {
            val fragment = OvoAddNameFragment()
            bundle?.run {
                return fragment.apply {
                    arguments = this@run
                }
            }
            return fragment
        }
    }
}
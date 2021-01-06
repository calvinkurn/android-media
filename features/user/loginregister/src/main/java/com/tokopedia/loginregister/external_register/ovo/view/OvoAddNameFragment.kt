package com.tokopedia.loginregister.external_register.ovo.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.external_register.base.activity.ExternalRegisterWebViewActivity
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.fragment.BaseAddNameFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseAddNameListener
import com.tokopedia.loginregister.external_register.ovo.data.ActivateOvoData
import com.tokopedia.loginregister.external_register.ovo.viewmodel.OvoAddNameViewModel
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
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
        activity?.run {
            val intent = Intent(this, ExternalRegisterWebViewActivity::class.java).apply {
                putExtra(ExternalRegisterConstants.PARAM.URL, url)
            }
            startActivity(intent)
        }
    }

    fun onSuccessActivateOvo(data: ActivateOvoData){
        goToExternalWebView("${data.activationUrl}?k=${data.goalKey}")
    }

    fun onErrorActivateOvo(error: Throwable) {
//        gotoAdd
    }

//    private fun goToAddName(uuid: String) {
//        if (activity != null) {
//            val applink = ApplinkConstInternalGlobal.ADD_NAME_REGISTER
//            val intent = RouteManager.getIntent(getContext(), applink)
//            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, phoneNumber)
//            intent.putExtra(ApplinkConstInternalGlobal.PARAM_UUID, uuid)
//            startActivityForResult(intent, RegisterInitialFragment.REQUEST_ADD_NAME_REGISTER_PHONE)
//        }
//    }

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
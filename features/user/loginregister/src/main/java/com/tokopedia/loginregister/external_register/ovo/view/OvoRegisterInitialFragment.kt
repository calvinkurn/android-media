package com.tokopedia.loginregister.external_register.ovo.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.viewmodel.ExternalRegisterViewModel
import com.tokopedia.loginregister.external_register.ovo.viewmodel.OvoAddNameViewModel
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

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
        if(::externalRegisterPreference.isInitialized
                && externalRegisterPreference.getGoalKey().isNotEmpty()
                && externalRegisterPreference.getAuthCode().isNotEmpty()) {
                    externalRegisterViewModel.register()
        }
        else {
            activity?.finish()
        }
    }

    private fun initObserver(){
        externalRegisterViewModel.registerRequestResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success -> onSuccessOvoRegister()
                is Fail -> onFailedOvoRegister()
            }
        })
    }

    fun onSuccessOvoRegister(){
        startActivity(Intent(activity, OvoFinalPageActivity::class.java))
        activity?.finish()
    }

    fun onFailedOvoRegister(){

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
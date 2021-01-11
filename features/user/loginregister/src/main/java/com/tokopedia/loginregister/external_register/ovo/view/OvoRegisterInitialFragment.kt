package com.tokopedia.loginregister.external_register.ovo.view

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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.viewmodel.ExternalRegisterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.URLDecoder
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
                is Success -> onSuccessOvoRegister()
                is Fail -> onFailedOvoRegister()
            }
        })
    }

    fun onSuccessOvoRegister(){
        val intent = OvoFinalPageActivity.createIntentSuccess(activity)
        startActivity(intent)
        activity?.finish()
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
package com.tokopedia.loginregister.external_register.ovo.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.BaseDialogConnectAccount
import com.tokopedia.loginregister.external_register.base.di.ExternalRegisterComponent
import com.tokopedia.loginregister.external_register.base.fragment.BaseAddPhoneEmailFragment
import com.tokopedia.loginregister.external_register.base.listener.BaseAddPhoneListener
import com.tokopedia.loginregister.external_register.base.listener.BaseDialogConnectAccListener
import com.tokopedia.loginregister.external_register.ovo.viewmodel.OvoAddNameViewModel
import com.tokopedia.loginregister.external_register.ovo.viewmodel.OvoAddPhoneViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 17/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class OvoAddPhoneFragment : BaseAddPhoneEmailFragment(), BaseAddPhoneListener, BaseDialogConnectAccListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }

    private val addPhoneViewModel by lazy {
        viewModelProvider.get(OvoAddPhoneViewModel::class.java)
    }

    override fun initInjector() {
        getComponent(ExternalRegisterComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseAddPhoneListener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPhoneViewModel.checkOvoResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success -> {

                }
                is Fail -> {

                }
            }
        })
    }

    fun onHasOvoAccount() {
        activity?.run {
            val dialog = BaseDialogConnectAccount(
                    listener = this@OvoAddPhoneFragment
            ).apply {
                setTitle(context?.getString(R.string.ovo_connect_account_title) ?: "")
                setDialogDescription(context?.getString(R.string.ovo_connect_account_description)
                        ?: "")
                setDialogDrawable(R.drawable.img_ovo_collaboration)
            }
            dialog.show(supportFragmentManager, "")
        }
    }

    override fun onDialogPositiveBtnClicked() {
        startActivity(Intent(context!!, OvoFinalPageActivity::class.java))
    }

    override fun onDialogNegativeBtnClicked() {
        startActivity(Intent(context!!, OvoFinalPageActivity::class.java))
    }

    override fun onAddPhoneNextButtonClicked() {
//        addPhoneViewModel.checkOvo()
    }

    override fun onAddPhoneOtherMethodButtonClicked() {
        startActivity(Intent(context!!, OvoAddNameActivity::class.java))
    }

    companion object {
        fun createInstance(bundle: Bundle? = null): Fragment {
            val fragment = OvoAddPhoneFragment()
            bundle?.run {
                return fragment.apply {
                    arguments = this@run
                }
            }
            return fragment
        }
    }

}
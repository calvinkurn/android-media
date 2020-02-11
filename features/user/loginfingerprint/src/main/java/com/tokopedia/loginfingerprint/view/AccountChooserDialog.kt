package com.tokopedia.loginfingerprint.view


import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler

import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.di.DaggerLoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintSettingModule
import com.tokopedia.loginfingerprint.viewmodel.AccountChooserViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_account_chooser.*
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-01-29.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AccountChooserDialog(val mContext: FragmentActivity) : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(AccountChooserViewModel::class.java) }

    init {
        setChild(View.inflate(mContext, R.layout.fragment_account_chooser, null))
        setTitle(getString(R.string.title_choose_account))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        DaggerLoginFingerprintComponent
                .builder()
                .loginFingerprintSettingModule(LoginFingerprintSettingModule())
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fp_account_chooser_parent.setOnClickListener {
            val fpDialog = ScanFingerprintDialog(mContext, null)
            fpDialog.showWithMode(ScanFingerprintDialog.MODE_LOGIN)
            dismiss()
        }

        setCloseClickListener { dismiss() }
        viewModel.latestAccount.observe(this, Observer {
            ImageHandler.loadImageCircle2(context, fp_account_chooser_avatar, it.profilePicture)
            fp_account_chooser_name.text = it.name
            fp_account_chooser_email.text = it.email
        })

    }
}

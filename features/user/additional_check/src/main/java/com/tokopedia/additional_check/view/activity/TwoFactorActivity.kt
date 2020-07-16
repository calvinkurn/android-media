package com.tokopedia.additional_check.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.view.TwoFactorFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class TwoFactorActivity: BaseSimpleActivity() {

//    override fun getComponent(): LoginFingerprintComponent {
//        return DaggerLoginFingerprintComponent.builder()
//                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
//                .loginFingerprintSettingModule(LoginFingerprintSettingModule(this))
//                .build()
//    }

    var enableBackBtn: Boolean? = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableBackBtn = intent?.extras?.getParcelable<TwoFactorResult>(TwoFactorFragment.RESULT_POJO_KEY)?.showSkipButton
    }

    override fun getNewFragment(): Fragment? {
        return TwoFactorFragment.newInstance(intent?.extras)
    }

    override fun onBackPressed() {
        if(enableBackBtn == true) {
            super.onBackPressed()
        }
    }

}
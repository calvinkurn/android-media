package com.tokopedia.loginregister.login.view.activity

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.view.fragment.SellerSeamlessLoginFragment

/**
 * Created by Yoris Prayogo on 20/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SellerSeamlessLoginActivity : BaseSimpleActivity(), HasComponent<LoginRegisterComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        return SellerSeamlessLoginFragment.createInstance(bundle)
    }

    override fun onBackPressed() {
        if(fragment != null && fragment is SellerSeamlessLoginFragment){
            (fragment as SellerSeamlessLoginFragment).onBackPressedFragment()
        }else{
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        setWhiteStatusBarIfSellerApp()
    }

    private fun setWhiteStatusBarIfSellerApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && GlobalConfig.isSellerApp()) {
            setStatusBarColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            setLightStatusBar(true)
        }
    }

    override fun getComponent(): LoginRegisterComponent {
        return DaggerLoginRegisterComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }
}
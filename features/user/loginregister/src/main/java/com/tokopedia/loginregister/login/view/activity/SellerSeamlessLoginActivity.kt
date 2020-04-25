package com.tokopedia.loginregister.login.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
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

    override fun getComponent(): LoginRegisterComponent {
        return DaggerLoginRegisterComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }
}
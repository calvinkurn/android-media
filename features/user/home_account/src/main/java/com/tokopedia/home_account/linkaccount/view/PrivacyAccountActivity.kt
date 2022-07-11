package com.tokopedia.home_account.linkaccount.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.linkaccount.di.DaggerLinkAccountComponent
import com.tokopedia.home_account.linkaccount.di.LinkAccountComponent
import com.tokopedia.home_account.linkaccount.di.module.LinkAccountModule

class PrivacyAccountActivity : BaseSimpleActivity(), HasComponent<LinkAccountComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
    }

    override fun getNewFragment(): Fragment =
        PrivacyAccountFragment.newInstance()

    override fun getComponent(): LinkAccountComponent =
        DaggerLinkAccountComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .linkAccountModule(LinkAccountModule(this))
            .build()

}
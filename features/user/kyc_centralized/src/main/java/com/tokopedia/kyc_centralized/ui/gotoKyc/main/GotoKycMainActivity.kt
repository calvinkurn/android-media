package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.GoToKycComponent

class GotoKycMainActivity: BaseSimpleActivity(), HasComponent<GoToKycComponent> {

    override fun getLayoutRes(): Int = R.layout.activity_goto_kyc_main

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): GoToKycComponent =
        ActivityComponentFactory.instance.createGoToKycActivityComponent(this)
}

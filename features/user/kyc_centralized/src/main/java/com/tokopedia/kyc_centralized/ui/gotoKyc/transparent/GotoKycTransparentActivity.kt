package com.tokopedia.kyc_centralized.ui.gotoKyc.transparent

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.GoToKycComponent

class GotoKycTransparentActivity: BaseSimpleActivity(), HasComponent<GoToKycComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment = GotoKycTransparentFragment.createInstance()

    override fun getComponent(): GoToKycComponent =
        ActivityComponentFactory.instance.createGoToKycActivityComponent(this)
}

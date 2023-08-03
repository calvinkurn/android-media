package com.tokopedia.kyc_centralized.ui.gotoKyc

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.webview.BaseSimpleWebViewActivity

class WebviewWithGotoKycActivity : BaseSimpleWebViewActivity(), HasComponent<GoToKycComponent> {

    override fun createFragmentInstance(): Fragment {
        return GotoKycWebWrapperFragment().newInstance(url, needLogin, allowOverride, pullToRefresh)
    }

    override fun getComponent(): GoToKycComponent {
        return ActivityComponentFactory.instance.createGoToKycActivityComponent(this)
    }
}

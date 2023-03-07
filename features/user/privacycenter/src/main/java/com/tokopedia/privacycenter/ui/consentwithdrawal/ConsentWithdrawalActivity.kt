package com.tokopedia.privacycenter.ui.consentwithdrawal

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.di.ActivityComponentFactory
import com.tokopedia.privacycenter.di.PrivacyCenterComponent

class ConsentWithdrawalActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {

    override fun getNewFragment(): Fragment? {
        return intent.data?.getQueryParameter(ApplinkConstInternalUserPlatform.GROUP_ID)?.let {
            ConsentWithdrawalFragment.createInstance(it.toIntOrZero())
        }
    }

    override fun getComponent(): PrivacyCenterComponent {
        return ActivityComponentFactory.instance.createComponent(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.consent_withdrawal_page_title))
    }
}

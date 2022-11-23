package com.tokopedia.privacycenter.dsar.ui

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.privacycenter.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.di.PrivacyCenterComponent

class DsarActivity: BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {
    override fun getNewFragment(): Fragment {
        return DsarFragment.newInstance()
    }

    override fun getComponent(): PrivacyCenterComponent {
        return DaggerPrivacyCenterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}

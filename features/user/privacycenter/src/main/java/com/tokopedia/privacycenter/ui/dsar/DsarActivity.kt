package com.tokopedia.privacycenter.ui.dsar

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.privacycenter.di.ActivityComponentFactory
import com.tokopedia.privacycenter.di.PrivacyCenterComponent

class DsarActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {
    override fun getNewFragment(): Fragment {
        return DsarFragment.newInstance()
    }

    override fun getComponent(): PrivacyCenterComponent {
        return ActivityComponentFactory.instance.createComponent(application)
    }
}

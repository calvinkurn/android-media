package com.tokopedia.privacycenter.searchhistory

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.privacycenter.common.di.ActivityComponentFactory
import com.tokopedia.privacycenter.common.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.common.di.PrivacyCenterComponent

class SearchHistoryActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {

    override fun getNewFragment(): Fragment = SearchHistoryFragment.newInstance()

    override fun getComponent(): PrivacyCenterComponent {
        return ActivityComponentFactory.instance.createComponent(application)
    }

}

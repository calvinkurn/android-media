package com.tokopedia.kyc_centralized.ui.tokoKyc.alacarte

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kyc_centralized.di.ActivityComponentFactory
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent

class UserIdentificationInfoSimpleActivity: BaseSimpleActivity(), HasComponent<UserIdentificationCommonComponent> {
    override fun getNewFragment(): Fragment {
        return UserIdentificationInfoSimpleFragment()
    }

    override fun getComponent(): UserIdentificationCommonComponent {
        return ActivityComponentFactory.instance.createActivityComponent(this)
    }
}

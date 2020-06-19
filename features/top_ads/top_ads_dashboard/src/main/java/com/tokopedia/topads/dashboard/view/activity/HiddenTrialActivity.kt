package com.tokopedia.topads.dashboard.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.fragment.HiddenTrialFragment

/**
 * Created by Pika on 14/5/20.
 */
class HiddenTrialActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {
    override fun getNewFragment(): Fragment? {
        return HiddenTrialFragment.newInstance(intent.extras)
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

}
package com.tokopedia.topads.debit.autotopup.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.view.fragment.TopAdsEditAutoTopUpFragment

/**
 * Created by Pika on 22/9/20.
 */
class TopAdsEditAutoTopUpActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {
    override fun getNewFragment(): Fragment = TopAdsEditAutoTopUpFragment.createInstance()
    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

}
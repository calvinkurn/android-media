package com.tokopedia.topads.credit.history.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.credit.history.view.fragment.TopAdsCreditHistoryFragment

class TopAdsCreditHistoryActivity: BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {
    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    override fun getNewFragment() = TopAdsCreditHistoryFragment.createInstance()

    companion object {
        fun createInstance(context: Context) = Intent(context, TopAdsCreditHistoryActivity::class.java)
    }
}
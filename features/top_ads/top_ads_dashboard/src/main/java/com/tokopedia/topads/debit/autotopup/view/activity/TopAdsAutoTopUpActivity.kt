package com.tokopedia.topads.debit.autotopup.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.view.fragment.TopAdsAutoTopUpFragment

class TopAdsAutoTopUpActivity: BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {

    override fun getNewFragment(): Fragment = TopAdsAutoTopUpFragment.createInstance()

    override fun getComponent(): TopAdsDashboardComponent  = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    companion object {
        fun createInstance(context: Context) = Intent(context, TopAdsAutoTopUpActivity::class.java)
    }
}
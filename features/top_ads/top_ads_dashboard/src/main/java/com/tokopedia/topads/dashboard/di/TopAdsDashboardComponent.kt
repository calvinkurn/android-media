package com.tokopedia.topads.dashboard.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAddCreditFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment
import com.tokopedia.topads.credit.history.view.fragment.TopAdsCreditHistoryFragment

import dagger.Component

/**
 * Created by hadi.putra on 23/04/18.
 */

@TopAdsDashboardScope
@Component(modules = [TopAdsDashboardModule::class, TopAdsDashboardShopModule::class,
    TopAdsDashboardNetworkModule::class, ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface TopAdsDashboardComponent {

    fun inject(topAdsDashboardFragment: TopAdsDashboardFragment)
    fun inject(topAdsAddCreditFragment: TopAdsAddCreditFragment)
    fun inject(topAdsCreditHistoryFragment: TopAdsCreditHistoryFragment)
}

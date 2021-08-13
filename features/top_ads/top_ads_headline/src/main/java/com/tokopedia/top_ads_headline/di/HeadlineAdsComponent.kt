package com.tokopedia.top_ads_headline.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.top_ads_headline.view.activity.EditFormHeadlineActivity
import com.tokopedia.top_ads_headline.view.fragment.*
import dagger.Component

@HeadlineAdsScope
@Component(modules = [HeadlineAdsModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface HeadlineAdsComponent {
    fun inject(adDetailsFragment: AdDetailsFragment)
    fun inject(adContentFragment: AdContentFragment)
    fun inject(topAdsHeadlineKeyFragment: TopAdsHeadlineKeyFragment)
    fun inject(adScheduleAndBudgetFragment: AdScheduleAndBudgetFragment)
    fun inject(topAdsProductListFragment: TopAdsProductListFragment)
    fun inject(editFormHeadlineActivity: EditFormHeadlineActivity)
    fun inject(editAdCostFragment: EditAdCostFragment)
    fun inject(editAdOthersFragment: EditAdOthersFragment)
    fun inject(headlineEditKeywordFragment: HeadlineEditKeywordFragment)
}
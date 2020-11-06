package com.tokopedia.top_ads_headline.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.top_ads_headline.view.fragment.AdContentFragment
import com.tokopedia.top_ads_headline.view.fragment.AdDetailsFragment
import com.tokopedia.top_ads_headline.view.fragment.TopAdsHeadlineKeyFragment
import com.tokopedia.top_ads_headline.view.fragment.TopAdsProductListFragment
import dagger.Component

@HeadlineAdsScope
@Component(modules = [HeadlineAdsModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface HeadlineAdsComponent {
    fun inject(adDetailsFragment: AdDetailsFragment)
    fun inject(adContentFragment: AdContentFragment)
    fun inject(topAdsProductListFragment: TopAdsProductListFragment)
    fun inject(topAdsHeadlineKeyFragment: TopAdsHeadlineKeyFragment)
}
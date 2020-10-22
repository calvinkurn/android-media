package com.example.top_ads_headline.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@HeadlineAdsScope
@Component(modules = [HeadlineAdsModule::class], dependencies = [BaseAppComponent::class])
interface HeadlineAdsComponent {

}
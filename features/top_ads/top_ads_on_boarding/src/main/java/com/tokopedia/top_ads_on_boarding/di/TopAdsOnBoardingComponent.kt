package com.tokopedia.top_ads_on_boarding.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.top_ads_on_boarding.view.activity.TopAdsOnBoardingActivity
import com.tokopedia.top_ads_on_boarding.view.fragment.AdsTypeFragment
import com.tokopedia.top_ads_on_boarding.view.fragment.AutoPsOnboardingFragment
import dagger.Component

@TopAdsOnBoardingScope
@Component(modules = [OnboardingViewModelModule::class, OnBoardingModule::class], dependencies = [BaseAppComponent::class])
interface TopAdsOnBoardingComponent {

    fun inject(adsTypeFragment: AdsTypeFragment)
    fun inject(topAdsOnBoardingActivity: TopAdsOnBoardingActivity)
    fun inject(autoPsOnboardingFragment: AutoPsOnboardingFragment)

}

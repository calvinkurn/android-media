package com.tokopedia.top_ads_on_boarding.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.top_ads_on_boarding.view.fragment.AdsTypeFragment
import dagger.Component

@TopAdsOnBoardingScope
@Component(dependencies = [BaseAppComponent::class])
interface TopAdsOnBoardingComponent {

    fun inject(adsTypeFragment: AdsTypeFragment)

}

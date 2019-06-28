package com.tokopedia.home_recom.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_recom.view.fragment.ProductInfoFragment
import com.tokopedia.home_recom.view.fragment.RecommendationFragment
import dagger.Component

@HomeRecommendationScope
@Component(modules = [
    HomeRecommendationModule::class,
    ViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface HomeRecommendationComponent{
    fun inject(fragment: RecommendationFragment)
    fun inject(fragment: ProductInfoFragment)
}
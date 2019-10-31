package com.tokopedia.home_recom.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_recom.view.fragment.ProductInfoFragment
import com.tokopedia.home_recom.view.fragment.RecommendationFragment
import com.tokopedia.home_recom.view.fragment.SimilarProductRecommendationFragment
import dagger.Component

/**
 * A Component class for Recommendation
 */
@HomeRecommendationScope
@Component(modules = [
    HomeRecommendationModule::class,
    ViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface HomeRecommendationComponent{
    fun inject(fragment: RecommendationFragment)
    fun inject(fragment: ProductInfoFragment)
    fun inject(fragment: SimilarProductRecommendationFragment)
}
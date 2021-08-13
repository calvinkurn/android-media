package com.tokopedia.home_recom.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_recom.view.fragment.RecommendationFragment
import com.tokopedia.home_recom.view.fragment.SimilarProductRecommendationFragment
import com.tokopedia.recommendation_widget_common.di.RecommendationCoroutineModule
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import dagger.Component

/**
 * A Component class for Recommendation
 */
@HomeRecommendationScope
@Component(modules = [
    HomeRecommendationModule::class,
    RecommendationCoroutineModule::class,
    RecommendationModule::class,
    ViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface HomeRecommendationComponent{
    fun inject(fragment: RecommendationFragment)
    fun inject(fragment: SimilarProductRecommendationFragment)
}
package com.tokopedia.home_recom.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_recom.view.recommendation.RecommendationFragment
import dagger.Component

@HomeRecommendationScope
@Component(modules = [
    HomeRecommendationModule::class,
    ViewModelModule::class,
    GraphQLRawModule::class
], dependencies = [BaseAppComponent::class])
interface HomeRecommendationComponent{
    fun inject(fragment: RecommendationFragment)
}
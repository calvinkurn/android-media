package com.tokopedia.categorylevels.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.categorylevels.domain.repository.CategoryFullFilterRepository
import com.tokopedia.categorylevels.domain.repository.CategoryProductCardsGqlRepository
import com.tokopedia.categorylevels.domain.repository.CategoryQuickFilterRepository
import com.tokopedia.recommendation_widget_common.di.RecommendationCoroutineModule
import com.tokopedia.topads.sdk.di.TopAdsUrlHitterModule
import dagger.Component

@CategoryNavScope
@Component(modules = [CategoryRevampModule::class, RecommendationCoroutineModule::class, TopAdsUrlHitterModule::class], dependencies = [BaseAppComponent::class])
interface CategoryRevampComponent {
    fun getCategoryProductCardsGqlRepository(): CategoryProductCardsGqlRepository
    fun getCategoryQuickFilterRepository(): CategoryQuickFilterRepository
    fun getCategoryFullFilterRepository(): CategoryFullFilterRepository
}
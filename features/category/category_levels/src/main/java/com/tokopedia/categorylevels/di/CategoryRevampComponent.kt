package com.tokopedia.categorylevels.di

import com.tokopedia.categorylevels.domain.repository.CategoryQuickFilterRepository
import com.tokopedia.categorylevels.domain.repository.CategoryProductCardsGqlRepository
import com.tokopedia.topads.sdk.di.TopAdsUrlHitterModule
import dagger.Component

@CategoryNavScope
@Component(modules = [CategoryRevampModule::class, TopAdsUrlHitterModule::class])
interface CategoryRevampComponent {
    fun getCategoryProductCardsGqlRepository(): CategoryProductCardsGqlRepository
    fun getCategoryQuickFilterRepository(): CategoryQuickFilterRepository
}
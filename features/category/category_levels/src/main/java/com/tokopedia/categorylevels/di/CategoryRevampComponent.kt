package com.tokopedia.categorylevels.di

import com.tokopedia.categorylevels.domain.repository.CategoryChipFilterRepository
import com.tokopedia.categorylevels.domain.repository.CategoryProductCardsGqlRepository
import dagger.Component

@CategoryNavScope
@Component(modules = [CategoryRevampModule::class])
interface CategoryRevampComponent {
    fun getCategoryProductCardsGqlRepository(): CategoryProductCardsGqlRepository
    fun getCategoryChipFilterRepository(): CategoryChipFilterRepository
}
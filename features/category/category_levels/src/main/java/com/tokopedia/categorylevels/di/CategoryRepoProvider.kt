package com.tokopedia.categorylevels.di

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.categorylevels.domain.repository.CategoryChildCategoriesRepository
import com.tokopedia.categorylevels.domain.repository.CategoryGqlPageRepository
import com.tokopedia.categorylevels.view.activity.CategoryNavActivity
import com.tokopedia.common.RepositoryProvider
import com.tokopedia.discovery2.repository.childcategory.ChildCategoryRepository
import com.tokopedia.discovery2.repository.chipfilter.ChipFilterRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository

class CategoryRepoProvider(val departmentName: String, val departmentId: String, val categoryUrl: String?) : RepositoryProvider {
    override fun providePageLoadTimePerformanceMonitoring(): PageLoadTimePerformanceInterface {
        return PageLoadTimePerformanceCallback(
                CategoryNavActivity.CATEGORY_LEVELS_PLT_PREPARE_METRICS,
                CategoryNavActivity.CATEGORY_LEVELS_PLT_NETWORK_METRICS,
                CategoryNavActivity.CATEGORY_LEVELS_PLT_RENDER_METRICS,0,0,0,0,null
        )
    }

    override fun provideDiscoveryPageRepository(getGQLString: (Int) -> String): DiscoveryPageRepository {
        return CategoryGqlPageRepository(departmentName, departmentId, categoryUrl)
    }

    override fun provideProductCardsRepository(): ProductCardsRepository {
        return DaggerCategoryRevampComponent.builder().build().getCategoryProductCardsGqlRepository()
    }

    override fun provideChipFilterRepository(): ChipFilterRepository {
        return DaggerCategoryRevampComponent.builder().build().getCategoryChipFilterRepository()
    }

    override fun provideChildCategoryRepository(): ChildCategoryRepository {
        return CategoryChildCategoriesRepository()
    }
}
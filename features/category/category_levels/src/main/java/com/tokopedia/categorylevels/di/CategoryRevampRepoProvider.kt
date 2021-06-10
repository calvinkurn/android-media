package com.tokopedia.categorylevels.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.categorylevels.domain.repository.CategoryEmptyStateRepository
import com.tokopedia.categorylevels.domain.repository.CategoryGqlPageRepository
import com.tokopedia.categorylevels.domain.usecase.CategoryTopAdsTrackingUseCase
import com.tokopedia.categorylevels.view.activity.CATEGORY_LEVELS_PLT_NETWORK_METRICS
import com.tokopedia.categorylevels.view.activity.CATEGORY_LEVELS_PLT_PREPARE_METRICS
import com.tokopedia.categorylevels.view.activity.CATEGORY_LEVELS_PLT_RENDER_METRICS
import com.tokopedia.common.RepositoryProvider
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class CategoryRevampRepoProvider(private val appComponent: BaseAppComponent, val departmentName: String, val departmentId: String, val categoryUrl: String?) : RepositoryProvider {
    override fun providePageLoadTimePerformanceMonitoring(): PageLoadTimePerformanceInterface {
        return PageLoadTimePerformanceCallback(
                CATEGORY_LEVELS_PLT_PREPARE_METRICS,
                CATEGORY_LEVELS_PLT_NETWORK_METRICS,
                CATEGORY_LEVELS_PLT_RENDER_METRICS,0,0,0,0,null
        )
    }

    override fun provideDiscoveryPageRepository(getGQLString: (Int) -> String): DiscoveryPageRepository {
        return CategoryGqlPageRepository(departmentName, departmentId, categoryUrl)
    }

    override fun provideProductCardsRepository(): ProductCardsRepository {
        return DaggerCategoryRevampComponent.builder().baseAppComponent(appComponent).build().getCategoryProductCardsGqlRepository()
    }

    override fun provideQuickFilterRepository(): QuickFilterRepository {
        return DaggerCategoryRevampComponent.builder().baseAppComponent(appComponent).build().getCategoryQuickFilterRepository()
    }

    override fun provideTopAdsTrackingUseCase(topAdsUrlHitter: TopAdsUrlHitter): TopAdsTrackingUseCase {
        return CategoryTopAdsTrackingUseCase(topAdsUrlHitter)
    }

    override fun provideEmptyStateRepository(): EmptyStateRepository {
        return CategoryEmptyStateRepository()
    }

    override fun provideFilterRepository(): FilterRepository {
        return DaggerCategoryRevampComponent.builder().baseAppComponent(appComponent).build().getCategoryFullFilterRepository()
    }
}
package com.tokopedia.discovery2.di

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.common.RepositoryProvider
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryDataGQLRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.emptystate.DiscoveryEmptyStateRepository
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsGQLRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.quickFilter.FilterGQLRepository
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterDiscoveryRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.usecase.topAdsUseCase.DiscoveryTopAdsTrackingUseCase
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_NETWORK_METRICS
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_PREPARE_METRICS
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_RENDER_METRICS
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class DiscoveryRepoProvider : RepositoryProvider {
    override fun providePageLoadTimePerformanceMonitoring(): PageLoadTimePerformanceInterface {
        return PageLoadTimePerformanceCallback(
                DISCOVERY_PLT_PREPARE_METRICS,
                DISCOVERY_PLT_NETWORK_METRICS,
                DISCOVERY_PLT_RENDER_METRICS,0,0,0,0,null
        )
    }

    override fun provideDiscoveryPageRepository(getGQLString: (Int) -> String): DiscoveryPageRepository {
        return DiscoveryDataGQLRepository(getGQLString)
    }

    override fun provideProductCardsRepository(): ProductCardsRepository {
        return ProductCardsGQLRepository()
    }

    override fun provideQuickFilterRepository(): QuickFilterRepository {
        return QuickFilterDiscoveryRepository()
    }

    override fun provideFilterRepository(): FilterRepository {
        return FilterGQLRepository()
    }

    override fun provideTopAdsTrackingUseCase(topAdsUrlHitter: TopAdsUrlHitter): TopAdsTrackingUseCase {
        return DiscoveryTopAdsTrackingUseCase(topAdsUrlHitter)
    }

    override fun provideEmptyStateRepository(): EmptyStateRepository {
        return DiscoveryEmptyStateRepository()
    }

}
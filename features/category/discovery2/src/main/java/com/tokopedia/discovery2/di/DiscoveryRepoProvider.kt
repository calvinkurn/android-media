package com.tokopedia.discovery2.di

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.common.RepositoryProvider
import com.tokopedia.discovery2.repository.childcategory.ChildCategoryRepository
import com.tokopedia.discovery2.repository.childcategory.DiscoveryChildCategoryRepository
import com.tokopedia.discovery2.repository.chipfilter.ChipFilterRepository
import com.tokopedia.discovery2.repository.chipfilter.ChipFilterRestRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryDataGQLRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRestRepository
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_NETWORK_METRICS
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_PREPARE_METRICS
import com.tokopedia.discovery2.viewcontrollers.activity.DISCOVERY_PLT_RENDER_METRICS

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
        return ProductCardsRestRepository()
    }

    override fun provideChipFilterRepository(): ChipFilterRepository {
        return ChipFilterRestRepository()
    }

    override fun provideChildCategoryRepository(): ChildCategoryRepository {
        return DiscoveryChildCategoryRepository()
    }

}
package com.tokopedia.common

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository

interface RepositoryProvider {
    fun providePageLoadTimePerformanceMonitoring(): PageLoadTimePerformanceInterface
    fun provideDiscoveryPageRepository(getGQLString: (Int) -> String): DiscoveryPageRepository
    fun provideProductCardsRepository(): ProductCardsRepository
}
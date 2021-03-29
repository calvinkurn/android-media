package com.tokopedia.common

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

interface RepositoryProvider {
    fun providePageLoadTimePerformanceMonitoring(): PageLoadTimePerformanceInterface
    fun provideDiscoveryPageRepository(getGQLString: (Int) -> String): DiscoveryPageRepository
    fun provideProductCardsRepository(): ProductCardsRepository
    fun provideQuickFilterRepository(): QuickFilterRepository
    fun provideTopAdsTrackingUseCase(topAdsUrlHitter: TopAdsUrlHitter): TopAdsTrackingUseCase
    fun provideEmptyStateRepository(): EmptyStateRepository
    fun provideFilterRepository(): FilterRepository
}
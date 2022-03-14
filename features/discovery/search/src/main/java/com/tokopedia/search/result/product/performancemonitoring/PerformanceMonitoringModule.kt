package com.tokopedia.search.result.product.performancemonitoring

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class PerformanceMonitoringModule(
    private val performanceMonitoring: PageLoadTimePerformanceInterface?
) {

    @Provides
    @SearchScope
    fun providePerformanceMonitoring(): PerformanceMonitoringProvider {
        return PerformanceMonitoringProvider { performanceMonitoring }
    }
}
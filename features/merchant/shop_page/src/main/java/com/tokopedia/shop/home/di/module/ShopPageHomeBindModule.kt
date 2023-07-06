package com.tokopedia.shop.home.di.module

import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalyticImpl
import com.tokopedia.shop.home.di.scope.ShopPageHomeScope
import dagger.Binds
import dagger.Module

@Module
abstract class ShopPageHomeBindModule {

    @ShopPageHomeScope
    @Binds
    abstract fun bindPlayPerformanceDashboardAnalytic(analytic: PlayPerformanceDashboardEntryPointAnalyticImpl): PlayPerformanceDashboardEntryPointAnalytic
}

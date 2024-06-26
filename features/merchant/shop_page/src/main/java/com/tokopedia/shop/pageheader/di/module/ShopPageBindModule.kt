package com.tokopedia.shop.pageheader.di.module

import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalyticImpl
import com.tokopedia.shop.pageheader.di.scope.ShopPageHeaderScope
import dagger.Binds
import dagger.Module

@Module
abstract class ShopPageBindModule {

    @ShopPageHeaderScope
    @Binds
    abstract fun bindPlayPerformanceDashboardAnalytic(analytic: PlayPerformanceDashboardEntryPointAnalyticImpl): PlayPerformanceDashboardEntryPointAnalytic

}

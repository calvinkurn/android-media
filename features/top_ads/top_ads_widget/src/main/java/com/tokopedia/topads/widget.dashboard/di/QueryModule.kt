package com.tokopedia.topads.widget.dashboard.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.topads.widget.R
import com.tokopedia.topads.widget.dashboard.internal.QueryObject
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Author errysuprayogi on 25,October,2019
 */
@Module
class QueryModule {
    @DashboardWidgetScope
    @Provides
    @IntoMap
    @StringKey(QueryObject.QUERY_TOPADS_DEPOSIT)
    fun queryDeposit(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.topads_deposit_query)

    @DashboardWidgetScope
    @Provides
    @IntoMap
    @StringKey(QueryObject.QUERY_TOPADS_STATISTIC)
    fun queryStatistic(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.topads_dashboard_statistic_query)
}
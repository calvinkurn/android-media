package com.tokopedia.notifications.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.notifications.R
import com.tokopedia.notifications.di.scope.CMNotificationContext
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module class GraphQueryModule {

    @Provides
    @Named(ATTRIBUTION_QUERY)
    fun provideAttributionQuery(
            @CMNotificationContext context: Context
    ): String {
        return GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_notification_attribution
        )
    }

    @Provides
    @Named(ATC_MUTATION_QUERY)
    fun provideAtcQuery(
            @CMNotificationContext context: Context
    ): String {
        return GraphqlHelper.loadRawString(
                context.resources,
                com.tokopedia.atc_common.R.raw.mutation_add_to_cart
        )
    }

    @Provides
    @Named(AMPLIFICATION_QUERY)
    fun provideAmplificationQuery(
            @CMNotificationContext context: Context
    ): String {
        return GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_notification_amplification
        )
    }

    companion object {
        const val ATTRIBUTION_QUERY = "query_attribution"
        const val ATC_MUTATION_QUERY = "query_atcMutation"
        const val AMPLIFICATION_QUERY = "query_amplification"
    }

}
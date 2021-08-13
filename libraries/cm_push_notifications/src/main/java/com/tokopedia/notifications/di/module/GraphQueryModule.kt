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
        const val AMPLIFICATION_QUERY = "query_amplification"
    }

}
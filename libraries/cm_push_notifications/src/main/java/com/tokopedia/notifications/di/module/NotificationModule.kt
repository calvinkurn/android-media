package com.tokopedia.notifications.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.R
import com.tokopedia.notifications.data.AttributionManager
import com.tokopedia.notifications.data.model.AttributionNotifier
import com.tokopedia.notifications.di.scope.CMNotificationContext
import com.tokopedia.notifications.di.scope.CMNotificationScope
import com.tokopedia.notifications.domain.AttributionUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module class NotificationModule(val context: Context) {

    @Provides
    @CMNotificationContext
    fun provideCMNotificationContext(): Context {
        return context
    }

    @Provides
    @CMNotificationScope
    fun provideGraphqlUseCase(): GraphqlUseCase<AttributionNotifier> {
        return GraphqlUseCase(
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @Provides
    @CMNotificationScope
    fun provideAttributionUseCase(
            useCase: GraphqlUseCase<AttributionNotifier>,
            @Named(NOTIFICATION_ATTRIBUTION) query: String
    ): AttributionUseCase {
        return AttributionUseCase(useCase, query)
    }

    @Provides
    @Named(NOTIFICATION_ATTRIBUTION)
    fun provideAttributionQuery(
            @CMNotificationContext context: Context
    ): String {
        return GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_notification_attribution
        )
    }

    @Provides
    @CMNotificationScope
    fun provideAttributionManager(useCase: AttributionUseCase): AttributionManager {
        return AttributionManager(useCase)
    }

    companion object {
        const val NOTIFICATION_ATTRIBUTION = "notification_attribution"
    }

}
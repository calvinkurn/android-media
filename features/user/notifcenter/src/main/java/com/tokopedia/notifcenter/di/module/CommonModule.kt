package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.analytics.NotificationTransactionAnalytics
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.util.CacheManager
import com.tokopedia.notifcenter.util.coroutines.AppDispatcherProvider
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module class CommonModule(val context: Context) {

    @Provides
    @NotificationContext
    fun provideNotificationContext(): Context {
        return context
    }

    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideDispatchers(): DispatcherProvider = AppDispatcherProvider()

    @Provides
    fun provideTransactionAnalytics() = NotificationTransactionAnalytics()

    @Provides
    fun provideUserSession(@NotificationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideCacheManager(@NotificationContext context: Context): CacheManager {
        return CacheManager(context)
    }

}
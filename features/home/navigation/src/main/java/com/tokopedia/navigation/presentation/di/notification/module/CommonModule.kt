package com.tokopedia.navigation.presentation.di.notification.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.navigation.analytics.NotificationTransactionAnalytics
import com.tokopedia.navigation.util.coroutines.AppDispatcherProvider
import com.tokopedia.navigation.util.coroutines.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module class CommonModule {

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

}
package com.tokopedia.searchbar.navigation_component.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.searchbar.navigation_component.data.notification.NotificationResponse
import com.tokopedia.searchbar.navigation_component.di.NavigationScope
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.searchbar.navigation_component.domain.QueryNotification
import dagger.Module
import dagger.Provides

@Module
class NavigationUseCaseModule {
    @NavigationScope
    @Provides
    fun provideGetNotificationUseCase(graphqlRepository: GraphqlRepository): GetNotificationUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<NotificationResponse>(graphqlRepository)
        useCase.setGraphqlQuery(QueryNotification.query)
        return GetNotificationUseCase(useCase)
    }
}
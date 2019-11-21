package com.tokopedia.navigation.presentation.di.notification.module

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.navigation.data.consts.NotificationQueriesConstant
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.domain.NotificationTransactionUseCase
import com.tokopedia.navigation.presentation.di.notification.scope.NotificationTransactionScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class NotificationTransactionModule {

    @Provides
    @NotificationTransactionScope
    fun provideGraphqlNotifTransactionUseCase(
            repository: GraphqlRepository): GraphqlUseCase<NotificationEntity> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationTransactionScope
    fun provideNotificationTransactionUseCase(
            @Named(NotificationQueriesConstant.DRAWER_PUSH_NOTIFICATION)
            query: String,
            useCase: GraphqlUseCase<NotificationEntity>): NotificationTransactionUseCase {
        return NotificationTransactionUseCase(query, useCase)
    }

}
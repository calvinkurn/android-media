package com.tokopedia.notifcenter.di.module

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.NotificationCenterDetail
import com.tokopedia.notifcenter.data.entity.NotificationEntity
import com.tokopedia.notifcenter.data.entity.NotificationUpdateFilter
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.domain.NotificationFilterUseCase
import com.tokopedia.notifcenter.domain.NotificationInfoTransactionUseCase
import com.tokopedia.notifcenter.domain.NotificationTransactionUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module class NotificationTransactionModule {

    @Provides
    @NotificationScope
    fun provideGraphqlInfoNotifTransactionUseCase(
            repository: GraphqlRepository): GraphqlUseCase<NotificationEntity> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideGraphqlNotifTransactionUseCase(
            repository: GraphqlRepository): GraphqlUseCase<NotificationCenterDetail> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideGraphqlNotifFilterTransactionUseCase(
            repository: GraphqlRepository): GraphqlUseCase<NotificationUpdateFilter> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideInfoNotificationTransactionUseCase(
            @Named(NotificationQueriesConstant.DRAWER_PUSH_NOTIFICATION)
            query: String,
            useCase: GraphqlUseCase<NotificationEntity>): NotificationInfoTransactionUseCase {
        return NotificationInfoTransactionUseCase(query, useCase)
    }

    @Provides
    @NotificationScope
    fun provideNotificationTransactionUseCase(
            @Named(NotificationQueriesConstant.TRANSACTION_NOTIFICATION)
            query: String,
            useCase: GraphqlUseCase<NotificationCenterDetail>): NotificationTransactionUseCase {
        return NotificationTransactionUseCase(query, useCase)
    }

    @Provides
    @NotificationScope
    fun provideNotificationFilterTransactionUseCase(
            @Named(NotificationQueriesConstant.FILTER_NOTIFICATION)
            query: String,
            useCase: GraphqlUseCase<NotificationUpdateFilter>): NotificationFilterUseCase {
        return NotificationFilterUseCase(query, useCase)
    }

}
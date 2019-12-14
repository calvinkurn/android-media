package com.tokopedia.notifcenter.presentation.di.notification.module

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.NotificationEntity
import com.tokopedia.notifcenter.domain.NotificationFilterUseCase
import com.tokopedia.notifcenter.domain.NotificationInfoTransactionUseCase
import com.tokopedia.notifcenter.domain.NotificationTransactionUseCase
import com.tokopedia.notifcenter.domain.pojo.NotificationCenterDetail
import com.tokopedia.notifcenter.domain.pojo.NotificationUpdateFilter
import com.tokopedia.notifcenter.presentation.di.notification.scope.NotificationTransactionScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class NotificationTransactionModule {

    @Provides
    @NotificationTransactionScope
    fun provideGraphqlInfoNotifTransactionUseCase(
            repository: GraphqlRepository): GraphqlUseCase<NotificationEntity> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationTransactionScope
    fun provideGraphqlNotifTransactionUseCase(
            repository: GraphqlRepository): GraphqlUseCase<NotificationCenterDetail> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationTransactionScope
    fun provideGraphqlNotifFilterTransactionUseCase(
            repository: GraphqlRepository): GraphqlUseCase<NotificationUpdateFilter> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationTransactionScope
    fun provideInfoNotificationTransactionUseCase(
            @Named(NotificationQueriesConstant.DRAWER_PUSH_NOTIFICATION)
            query: String,
            useCase: GraphqlUseCase<NotificationEntity>): NotificationInfoTransactionUseCase {
        return NotificationInfoTransactionUseCase(query, useCase)
    }

    @Provides
    @NotificationTransactionScope
    fun provideNotificationTransactionUseCase(
            @Named(NotificationQueriesConstant.TRANSACTION_NOTIFICATION)
            query: String,
            useCase: GraphqlUseCase<NotificationCenterDetail>): NotificationTransactionUseCase {
        return NotificationTransactionUseCase(query, useCase)
    }

    @Provides
    @NotificationTransactionScope
    fun provideNotificationFilterTransactionUseCase(
            @Named(NotificationQueriesConstant.FILTER_NOTIFICATION)
            query: String,
            useCase: GraphqlUseCase<NotificationUpdateFilter>): NotificationFilterUseCase {
        return NotificationFilterUseCase(query, useCase)
    }

}
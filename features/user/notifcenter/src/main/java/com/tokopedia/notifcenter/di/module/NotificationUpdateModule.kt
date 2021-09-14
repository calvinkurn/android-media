package com.tokopedia.notifcenter.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.NotificationCenterSingleDetail
import com.tokopedia.notifcenter.data.entity.ProductStockHandler
import com.tokopedia.notifcenter.data.entity.ProductStockReminder
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.domain.SingleNotificationUpdateUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase as UseCase

@Module
class NotificationUpdateModule {

    @Provides
    @NotificationScope
    fun provideGraphqlProductStockHandlerUseCase(
            repository: GraphqlRepository): UseCase<ProductStockHandler> {
        return UseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideGraphqlProductStockReminderUseCase(
            repository: GraphqlRepository): UseCase<ProductStockReminder> {
        return UseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideSingleNotificationUpdateUseCase(
            @Named(NotificationQueriesConstant.SINGLE_NOTIFICATION_UPDATE)
            query: String,
            useCase: UseCase<NotificationCenterSingleDetail>
    ): SingleNotificationUpdateUseCase {
        return SingleNotificationUpdateUseCase(query, useCase)
    }

}

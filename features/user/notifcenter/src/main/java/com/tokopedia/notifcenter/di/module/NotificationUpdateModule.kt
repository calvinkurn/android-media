package com.tokopedia.notifcenter.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.data.entity.ProductStockHandler
import com.tokopedia.notifcenter.data.entity.ProductStockReminder
import com.tokopedia.notifcenter.di.scope.NotificationScope
import dagger.Module
import dagger.Provides
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

}

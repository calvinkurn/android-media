package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.NotificationCenterSingleDetail
import com.tokopedia.notifcenter.data.entity.ProductHighlightItem
import com.tokopedia.notifcenter.data.entity.ProductStockHandler
import com.tokopedia.notifcenter.data.entity.ProductStockReminder
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.domain.ProductHighlightUseCase
import com.tokopedia.notifcenter.domain.ProductStockHandlerUseCase
import com.tokopedia.notifcenter.domain.ProductStockReminderUseCase
import com.tokopedia.notifcenter.domain.SingleNotificationUpdateUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase as UseCase

@Module
class NotificationUpdateModule {

    @Provides
    @Named("atcMutation")
    @NotificationScope
    fun provideAddToCartMutation(@NotificationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
    }

    @Provides
    @NotificationScope
    fun provideGraphqlProductStockHandlerUseCase(
            repository: GraphqlRepository): UseCase<ProductStockHandler> {
        return UseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideProductStockHandlerUseCase(
            @Named(NotificationQueriesConstant.PRODUCT_STOCK_HANDLER)
            query: String,
            useCase: UseCase<ProductStockHandler>
    ): ProductStockHandlerUseCase {
        return ProductStockHandlerUseCase(query, useCase)
    }

    @Provides
    @NotificationScope
    fun provideGraphqlProductStockReminderUseCase(
            repository: GraphqlRepository): UseCase<ProductStockReminder> {
        return UseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideProductStockReminderUseCase(
            @Named(NotificationQueriesConstant.PRODUCT_STOCK_REMINDER)
            query: String,
            useCase: UseCase<ProductStockReminder>
    ): ProductStockReminderUseCase {
        return ProductStockReminderUseCase(query, useCase)
    }

    @Provides
    @NotificationScope
    fun provideProductHighlightUseCase(
            @Named(NotificationQueriesConstant.PRODUCT_HIGHLIGHT)
            query: String,
            useCase: UseCase<ProductHighlightItem>
    ): ProductHighlightUseCase {
        return ProductHighlightUseCase(query, useCase)
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

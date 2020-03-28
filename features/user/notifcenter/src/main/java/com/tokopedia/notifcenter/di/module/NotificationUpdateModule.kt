package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.ProductStockHandler
import com.tokopedia.notifcenter.data.entity.ProductStockReminder
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.domain.ProductStockHandlerUseCase
import com.tokopedia.notifcenter.domain.ProductStockReminderUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module class NotificationUpdateModule {

    @Provides
    @Named("atcMutation")
    @NotificationScope
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_product_to_cart)
    }

    @Provides
    @NotificationScope
    fun provideGraphqlProductStockHandlerUseCase(
            repository: GraphqlRepository): GraphqlUseCase<ProductStockHandler> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideProductStockHandlerUseCase(
            @Named(NotificationQueriesConstant.PRODUCT_STOCK_HANDLER)
            query: String,
            useCase: GraphqlUseCase<ProductStockHandler>
    ): ProductStockHandlerUseCase {
        return ProductStockHandlerUseCase(query, useCase)
    }

    @Provides
    @NotificationScope
    fun provideGraphqlProductStockReminderUseCase(
            repository: GraphqlRepository): GraphqlUseCase<ProductStockReminder> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @NotificationScope
    fun provideProductStockReminderUseCase(
            @Named(NotificationQueriesConstant.PRODUCT_STOCK_REMINDER)
            query: String,
            useCase: GraphqlUseCase<ProductStockReminder>
    ): ProductStockReminderUseCase {
        return ProductStockReminderUseCase(query, useCase)
    }

}


package com.tokopedia.watch.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.watch.orderlist.mapper.OrderListMapper
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase
import com.tokopedia.watch.ordersummary.mapper.SummaryMapper
import com.tokopedia.watch.ordersummary.usecase.GetSummaryUseCase
import dagger.Module
import dagger.Provides

@Module
class TkpdWatchUseCaseModule {
    @TkpdWatchScope
    @Provides
    fun graphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @TkpdWatchScope
    fun provideOrderListMapper(
    ): OrderListMapper {
        return OrderListMapper()
    }

    @Provides
    @TkpdWatchScope
    fun provideGetOrderListUseCase(
        graphqlUseCase: GraphqlUseCase,
        orderListMapper: OrderListMapper
    ): GetOrderListUseCase {
        return GetOrderListUseCase(graphqlUseCase, orderListMapper)
    }

    @Provides
    @TkpdWatchScope
    fun provideSummaryMapper(
    ): SummaryMapper {
        return SummaryMapper()
    }

    @Provides
    @TkpdWatchScope
    fun provideGetSummaryUseCase(
        graphqlUseCase: GraphqlUseCase,
        summaryMapper: SummaryMapper
    ): GetSummaryUseCase {
        return GetSummaryUseCase(graphqlUseCase, summaryMapper)
    }

    @Provides
    @TkpdWatchScope
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }
}
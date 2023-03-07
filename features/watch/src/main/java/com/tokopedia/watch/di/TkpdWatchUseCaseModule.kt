
package com.tokopedia.watch.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.watch.orderlist.mapper.AcceptBulkOrderMapper
import com.tokopedia.watch.orderlist.mapper.AcceptBulkOrderStatusMapper
import com.tokopedia.watch.orderlist.mapper.OrderListMapper
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase
import com.tokopedia.watch.orderlist.usecase.SomListAcceptBulkOrderUseCase
import com.tokopedia.watch.orderlist.usecase.SomListGetAcceptBulkOrderStatusUseCase
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

    @TkpdWatchScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
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
    fun provideSomListAcceptBulkOrderUseCase(
        gqlRepository: GraphqlRepository,
        mapper: AcceptBulkOrderMapper
    ): SomListAcceptBulkOrderUseCase {
        return SomListAcceptBulkOrderUseCase(gqlRepository, mapper)
    }

    @Provides
    @TkpdWatchScope
    fun provideSomListGetAcceptBulkOrderStatusUseCase(
        gqlRepository: GraphqlRepository,
        mapper: AcceptBulkOrderStatusMapper
    ): SomListGetAcceptBulkOrderStatusUseCase {
        return SomListGetAcceptBulkOrderStatusUseCase(gqlRepository, mapper)
    }

    @Provides
    @TkpdWatchScope
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }
}
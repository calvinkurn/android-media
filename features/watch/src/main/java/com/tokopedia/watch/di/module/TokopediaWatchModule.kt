package com.tokopedia.watch.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.watch.di.scope.TokopediaWatchScope
import com.tokopedia.watch.orderlist.mapper.OrderListMapper
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase
import dagger.Module
import dagger.Provides

@Module
class TokopediaWatchModule {

    @TokopediaWatchScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @TokopediaWatchScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @TokopediaWatchScope
    @Provides
    fun provideMapper(): OrderListMapper {
        return OrderListMapper()
    }

    @TokopediaWatchScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @TokopediaWatchScope
    @Provides
    fun provideGetOrderListUseCase(graphqlUseCase: GraphqlUseCase, orderListMapper: OrderListMapper): GetOrderListUseCase {
        return GetOrderListUseCase(graphqlUseCase, orderListMapper)
    }

}
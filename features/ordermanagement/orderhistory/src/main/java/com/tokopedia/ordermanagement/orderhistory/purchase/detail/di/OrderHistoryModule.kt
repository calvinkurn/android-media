package com.tokopedia.ordermanagement.orderhistory.purchase.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.mapper.OrderDetailMapper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by kris on 11/17/17. Tokopedia
 */
@Module
class OrderHistoryModule {

    @Provides
    @OrderHistoryScope
    fun provideOrderDetailMapper(): OrderDetailMapper {
        return OrderDetailMapper()
    }

    @Provides
    @OrderHistoryScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @OrderHistoryScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
package com.tokopedia.ordermanagement.orderhistory.purchase.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.OrderHistoryRepository
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
    fun provideOrderDetailService(): OrderDetailService {
        return OrderDetailService()
    }

    @Provides
    @OrderHistoryScope
    fun provideOrderDetailMapper(): OrderDetailMapper {
        return OrderDetailMapper()
    }

    @Provides
    @OrderHistoryScope
    fun provideOrderHistoryRepository(): OrderHistoryRepository {
        return OrderHistoryRepository(
                provideOrderDetailService(),
                provideOrderDetailMapper())
    }

    @Provides
    @OrderHistoryScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @OrderHistoryScope
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}
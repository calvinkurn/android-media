package com.tokopedia.buyerorder.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.PREFERENCES_NAME
import com.tokopedia.buyerorder.detail.view.OrderDetailRechargeDownloadWebviewAnalytics
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor

@Module
class OrderDetailModule {

    @Provides
    fun providesSendEventNotificationUseCase(@ApplicationContext context: Context?): SendEventNotificationUseCase {
        val interceptorList: MutableList<Interceptor> = ArrayList(1)
        interceptorList.add(
            TkpdOldAuthInterceptor(context, context as NetworkRouter, UserSession(context))
        )
        return SendEventNotificationUseCase(interceptorList, context)
    }

    @Provides
    fun providesUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideOrderDetailRechargeDownloadWebviewAnalytics(): OrderDetailRechargeDownloadWebviewAnalytics {
        return OrderDetailRechargeDownloadWebviewAnalytics()
    }

    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context?): LocalCacheHandler {
        return LocalCacheHandler(context, PREFERENCES_NAME)
    }

    @Provides
    fun provideOrderListAnalytic() : OrderListAnalytics = OrderListAnalytics()

    @Provides
    fun provideRepository(): GraphqlRepository = getInstance().graphqlRepository

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
package com.tokopedia.buyerorder.recharge_download.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.common.BuyerProductionDispatcherProvider
import com.tokopedia.buyerorder.recharge_download.presentation.analytics.OrderDetailRechargeDownloadWebviewAnalytics
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 21/01/2021
 */
@Module
class OrderDetailRechargeDownloadWebviewModule {

    @OrderDetailRechargeDownloadWebviewScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @OrderDetailRechargeDownloadWebviewScope
    @Provides
    fun provideDispatcherProvider(): BuyerDispatcherProvider =
            BuyerProductionDispatcherProvider()

    @OrderDetailRechargeDownloadWebviewScope
    @Provides
    fun provideInvoiceAnalytics(): OrderDetailRechargeDownloadWebviewAnalytics =
            OrderDetailRechargeDownloadWebviewAnalytics()

    @OrderDetailRechargeDownloadWebviewScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

}
package com.tokopedia.buyerorder.recharge_download.di

import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.common.BuyerProductionDispatcherProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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

}
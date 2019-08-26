package com.tokopedia.purchase_platform.common.di2

import com.tokopedia.purchase_platform.common.data.common.api.CommonPurchaseApi
import com.tokopedia.purchase_platform.common.data.common.repository.CommonPurchaseRepository
import com.tokopedia.purchase_platform.common.data.common.repository.ICommonPurchaseRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@Module
class PurchasePlatformCommonNetworkModule {

    @Provides
    fun provideCartApi(retrofit: Retrofit): CommonPurchaseApi {
        return retrofit.create(CommonPurchaseApi::class.java)
    }

    @Provides
    fun provideCommonPurchaseRepository(commonPurchaseApi: CommonPurchaseApi): ICommonPurchaseRepository {
        return CommonPurchaseRepository(commonPurchaseApi)
    }

}
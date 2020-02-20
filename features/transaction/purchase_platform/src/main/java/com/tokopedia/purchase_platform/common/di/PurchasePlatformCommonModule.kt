package com.tokopedia.purchase_platform.common.di

import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseAkamaiApi
import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseApi
import com.tokopedia.purchase_platform.common.data.repository.CommonPurchaseRepository
import com.tokopedia.purchase_platform.common.data.repository.ICommonPurchaseRepository
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.CheckoutMapper
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.ICheckoutMapper
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@Module
class PurchasePlatformCommonModule {

    @Provides
    fun provideCommonPurchaseApi(@PurchasePlatformQualifier retrofit: Retrofit): CommonPurchaseApi {
        return retrofit.create(CommonPurchaseApi::class.java)
    }

    @Provides
    fun provideCommonPurchaseAkamaiApi(@PurchasePlatformAkamaiQualifier retrofit: Retrofit): CommonPurchaseAkamaiApi {
        return retrofit.create(CommonPurchaseAkamaiApi::class.java)
    }

    @Provides
    fun provideCommonPurchaseRepository(commonPurchaseApi: CommonPurchaseApi, commonPurchaseAkamaiApi: CommonPurchaseAkamaiApi): ICommonPurchaseRepository {
        return CommonPurchaseRepository(commonPurchaseApi, commonPurchaseAkamaiApi)
    }

    @Provides
    fun provideICheckoutMapper(): ICheckoutMapper {
        return CheckoutMapper()
    }
}
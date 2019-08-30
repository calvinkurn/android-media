package com.tokopedia.purchase_platform.common.di2

import com.tokopedia.purchase_platform.common.base.IMapperUtil
import com.tokopedia.purchase_platform.common.base.MapperUtil
import com.tokopedia.purchase_platform.common.data.common.api.CommonPurchaseApi
import com.tokopedia.purchase_platform.common.data.common.repository.CommonPurchaseRepository
import com.tokopedia.purchase_platform.common.data.common.repository.ICommonPurchaseRepository
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
    fun provideCommonPurchaseRepository(commonPurchaseApi: CommonPurchaseApi): ICommonPurchaseRepository {
        return CommonPurchaseRepository(commonPurchaseApi)
    }

    @Provides
    fun provideICheckoutMapper(iMapperUtil: IMapperUtil): ICheckoutMapper {
        return CheckoutMapper(iMapperUtil)
    }
}
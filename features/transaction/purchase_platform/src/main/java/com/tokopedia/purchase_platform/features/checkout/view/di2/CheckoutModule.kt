package com.tokopedia.purchase_platform.features.checkout.view.di2

import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.purchase_platform.common.di2.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di2.PurchasePlatformCommonNetworkModule
import com.tokopedia.purchase_platform.features.checkout.data.api.CheckoutApi
import com.tokopedia.purchase_platform.features.checkout.data.repository.CheckoutRepository
import com.tokopedia.purchase_platform.features.checkout.data.repository.ICheckoutRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@Module(includes = [PromoCheckoutModule::class, PurchasePlatformBaseModule::class, PurchasePlatformCommonNetworkModule::class])
class CheckoutModule {

    @Provides
    @CheckoutScope
    fun provideCheckoutApi(retrofit: Retrofit): CheckoutApi {
        return retrofit.create(CheckoutApi::class.java)
    }

    @Provides
    @CheckoutScope
    fun provideICheckoutRepository(checkoutApi: CheckoutApi): ICheckoutRepository {
        return CheckoutRepository(checkoutApi)
    }

}
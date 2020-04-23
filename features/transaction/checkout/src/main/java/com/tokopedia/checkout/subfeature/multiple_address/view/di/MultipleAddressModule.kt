package com.tokopedia.checkout.subfeature.multiple_address.view.di

import com.tokopedia.checkout.data.api.CommonPurchaseAkamaiApi
import com.tokopedia.checkout.data.api.CommonPurchaseApi
import com.tokopedia.checkout.data.repository.CommonPurchaseRepository
import com.tokopedia.checkout.data.repository.ICommonPurchaseRepository
import com.tokopedia.checkout.subfeature.multiple_address.domain.mapper.CartMapper
import com.tokopedia.checkout.subfeature.multiple_address.domain.mapper.ICartMapper
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressUseCase
import com.tokopedia.checkout.subfeature.multiple_address.data.api.MultipleAddressApi
import com.tokopedia.checkout.subfeature.multiple_address.data.repository.IMultipleAddressRepository
import com.tokopedia.checkout.subfeature.multiple_address.data.repository.MultipleAddressRepository
import com.tokopedia.checkout.subfeature.multiple_address.domain.usecase.GetCartMultipleAddressListUseCase
import com.tokopedia.checkout.subfeature.multiple_address.view.IMultipleAddressPresenter
import com.tokopedia.checkout.subfeature.multiple_address.view.MultipleAddressPresenter
import com.tokopedia.purchase_platform.common.di.*
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@Module(includes = [
    PurchasePlatformNetworkModule::class,
    PurchasePlatformBaseModule::class
])
class MultipleAddressModule {

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
    @MultipleAddressScope
    fun provideMultipleAddressApi(@PurchasePlatformQualifier retrofit: Retrofit): MultipleAddressApi {
        return retrofit.create(MultipleAddressApi::class.java)
    }

    @Provides
    @MultipleAddressScope
    fun provideIMultipleAddressRepository(multipleAddressApi: MultipleAddressApi): IMultipleAddressRepository {
        return MultipleAddressRepository(multipleAddressApi)
    }

    @Provides
    @MultipleAddressScope
    fun provideICartMapper(): ICartMapper {
        return CartMapper()
    }

    @Provides
    @MultipleAddressScope
    fun providePresenter(changeShippingAddressUseCase: ChangeShippingAddressUseCase,
                         getCartMultipleAddressListUseCase: GetCartMultipleAddressListUseCase,
                         userSessionInterface: UserSessionInterface): IMultipleAddressPresenter {
        return MultipleAddressPresenter(
                getCartMultipleAddressListUseCase,
                changeShippingAddressUseCase,
                userSessionInterface
        )
    }

}
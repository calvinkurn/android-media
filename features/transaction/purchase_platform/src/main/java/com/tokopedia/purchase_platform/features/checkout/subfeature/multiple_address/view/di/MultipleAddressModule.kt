package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.di

import com.tokopedia.purchase_platform.common.base.IMapperUtil
import com.tokopedia.purchase_platform.common.di2.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di2.PurchasePlatformCommonModule
import com.tokopedia.purchase_platform.common.di2.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.di2.PurchasePlatformQualifier
import com.tokopedia.purchase_platform.common.utils.CartApiRequestParamGenerator
import com.tokopedia.purchase_platform.features.cart.domain.mapper.CartMapper
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.ChangeShippingAddressUseCase
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.api.MultipleAddressApi
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.repository.IMultipleAddressRepository
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.repository.MultipleAddressRepository
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.usecase.GetCartMultipleAddressListUseCase
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.IMultipleAddressPresenter
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.MultipleAddressPresenter
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@Module(includes = [
    PurchasePlatformNetworkModule::class,
    PurchasePlatformBaseModule::class,
    PurchasePlatformCommonModule::class
])
class MultipleAddressModule {

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
    fun provideICartMapper(mapperUtil: IMapperUtil): ICartMapper {
        return CartMapper(mapperUtil)
    }

    @Provides
    @MultipleAddressScope
    fun providePresenter(changeShippingAddressUseCase: ChangeShippingAddressUseCase,
                         getCartMultipleAddressListUseCase: GetCartMultipleAddressListUseCase,
                         cartApiRequestParamGenerator: CartApiRequestParamGenerator,
                         userSessionInterface: UserSessionInterface): IMultipleAddressPresenter {
        return MultipleAddressPresenter(
                getCartMultipleAddressListUseCase,
                changeShippingAddressUseCase,
                cartApiRequestParamGenerator,
                userSessionInterface
        )
    }

}
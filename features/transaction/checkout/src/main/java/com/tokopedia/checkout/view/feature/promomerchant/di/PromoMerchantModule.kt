package com.tokopedia.checkout.view.feature.promomerchant.di

import com.tokopedia.checkout.view.feature.promomerchant.view.PromoMerchantAdapter
import com.tokopedia.checkout.view.feature.promomerchant.view.PromoMerchantContract
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 27/02/19.
 */
@Module
class PromoMerchantModule {

    /*@Provides
    @PromoMerchantScope
    internal fun providePromoMerchantAdapter(): PromoMerchantAdapter {
        return PromoMerchantAdapter()
    }

    @Provides
    @PromoMerchantScope
    internal fun provideShippingDurationPresenter(): PromoMerchantContract.Presenter {
        return PromoMerchantPresenter()
    }*/

    /*@Provides
    @ShippingDurationScope
    internal fun provideShippingDurationConverter(): ShippingDurationConverter {
        return ShippingDurationConverter()
    }

    @Provides
    @ShippingDurationScope
    internal fun provideShippingCourierConverter(): ShippingCourierConverter {
        return ShippingCourierConverter()
    }

    @Provides
    @ShippingDurationScope
    internal fun getCourierRecommendationUseCase(shippingDurationConverter: ShippingDurationConverter): GetCourierRecommendationUseCase {
        return GetCourierRecommendationUseCase(shippingDurationConverter)
    }*/

}
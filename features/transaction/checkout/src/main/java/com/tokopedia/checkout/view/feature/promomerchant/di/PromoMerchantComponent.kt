package com.tokopedia.checkout.view.feature.promomerchant.di

import com.tokopedia.checkout.view.feature.promomerchant.view.PromoMerchantBottomsheet
import dagger.Component

/**
 * Created by fwidjaja on 27/02/19.
 */

@PromoMerchantScope
@Component(modules = [PromoMerchantModule::class])
interface PromoMerchantComponent {
    fun inject(promoMerchantBottomsheet: PromoMerchantBottomsheet)
}

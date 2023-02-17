package com.tokopedia.logisticcart.shipping.features.shippingcourier.di

import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet
import dagger.Component

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
@ShippingCourierScope
@Component(modules = [ShippingCourierModule::class])
interface ShippingCourierComponent {
    fun inject(shippingCourierBottomsheet: ShippingCourierBottomsheet)
}

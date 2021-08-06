package com.tokopedia.logisticcart.shipping.features.shippingduration.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet
import dagger.Component

/**
 * Created by Irfan Khoirul on 07/08/18.
 */
@ShippingDurationScope
@Component(dependencies = [BaseAppComponent::class], modules = [ShippingDurationModule::class])
interface ShippingDurationComponent {
    fun inject(shippingDurationBottomsheet: ShippingDurationBottomsheet?)
}
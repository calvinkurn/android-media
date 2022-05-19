package com.tokopedia.shop.flash_sale.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@ShopFlashSaleScope
@Component(modules = [ShopFlashSaleModule::class], dependencies = [BaseAppComponent::class])
interface ShopFlashSaleComponent {
}
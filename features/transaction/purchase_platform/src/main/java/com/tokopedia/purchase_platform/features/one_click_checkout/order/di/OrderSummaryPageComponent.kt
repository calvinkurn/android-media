package com.tokopedia.purchase_platform.features.one_click_checkout.order.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import dagger.Component

@OrderSummaryPageScope
@Component(modules = [OrderSummaryPageModule::class, OrderSummaryPageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface OrderSummaryPageComponent {

    fun inject(fragment: OrderSummaryPageFragment)
}
package com.tokopedia.oneclickcheckout.order.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageActivity
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import dagger.Component

@OrderSummaryPageScope
@Component(modules = [OrderSummaryPageModule::class, OrderSummaryPageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface OrderSummaryPageComponent {

    fun inject(fragment: OrderSummaryPageFragment)
    fun inject(activity: OrderSummaryPageActivity)
}
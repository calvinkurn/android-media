package com.tokopedia.orderhistory.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.orderhistory.view.fragment.OrderHistoryFragment
import dagger.Component

@OrderHistoryScope
@Component(
        modules = [
            OrderHistoryModule::class,
            OrderHistoryViewModelModule::class,
            OrderHistoryContextModule::class
        ],
        dependencies = [BaseAppComponent::class]
)
interface OrderHistoryComponent {
    fun inject(fragment: OrderHistoryFragment)
}
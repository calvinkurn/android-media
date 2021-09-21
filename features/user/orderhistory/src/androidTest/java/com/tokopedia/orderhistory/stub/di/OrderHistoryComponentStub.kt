package com.tokopedia.orderhistory.stub.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.orderhistory.di.OrderHistoryComponent
import com.tokopedia.orderhistory.di.OrderHistoryContextModule
import com.tokopedia.orderhistory.di.OrderHistoryScope
import com.tokopedia.orderhistory.di.OrderHistoryViewModelModule
import com.tokopedia.orderhistory.view.activity.base.OrderHistoryTest
import dagger.Component

@OrderHistoryScope
@Component(
        modules = [
            OrderHistoryModuleStub::class,
            OrderHistoryViewModelModule::class,
            OrderHistoryContextModule::class,
            OrderHistoryStubDependencyModule::class
        ],
        dependencies = [BaseAppComponent::class]
)
interface OrderHistoryComponentStub : OrderHistoryComponent {
    fun inject(fragment: OrderHistoryTest)
}
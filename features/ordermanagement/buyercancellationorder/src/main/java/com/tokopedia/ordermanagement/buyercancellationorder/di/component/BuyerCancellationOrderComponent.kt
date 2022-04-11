package com.tokopedia.ordermanagement.buyercancellationorder.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.ordermanagement.buyercancellationorder.di.module.GetCancellationReasonModule
import com.tokopedia.ordermanagement.buyercancellationorder.di.scope.BuyerCancellationOrderScope
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.fragment.BuyerRequestCancelFragment
import dagger.Component

@BuyerCancellationOrderScope
@Component(dependencies = [BaseAppComponent::class], modules = [GetCancellationReasonModule::class])
interface BuyerCancellationOrderComponent {
    fun inject(buyerRequestCancelFragment: BuyerRequestCancelFragment?)
}
package com.tokopedia.sellerorder.partial_order_fulfillment.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.bottomsheet.PofBottomSheet
import dagger.Component

@PofScope
@Component(
    modules = [PofViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface PofComponent {
    fun inject(pofBottomSheet: PofBottomSheet)
}

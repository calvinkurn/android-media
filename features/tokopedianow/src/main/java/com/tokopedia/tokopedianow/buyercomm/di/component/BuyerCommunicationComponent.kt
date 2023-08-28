package com.tokopedia.tokopedianow.buyercomm.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.buyercomm.di.module.BuyerCommunicationModule
import com.tokopedia.tokopedianow.buyercomm.di.module.BuyerCommunicationViewModelModule
import com.tokopedia.tokopedianow.buyercomm.presentation.bottomsheet.TokoNowBuyerCommunicationBottomSheet
import com.tokopedia.tokopedianow.buyercomm.di.scope.BuyerCommunicationScope
import dagger.Component

@BuyerCommunicationScope
@Component(
    modules = [
        BuyerCommunicationModule::class,
        BuyerCommunicationViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface BuyerCommunicationComponent {

    fun inject(bottomSheet: TokoNowBuyerCommunicationBottomSheet)
}

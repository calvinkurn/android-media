package com.tokopedia.sellerorder.buyer_request_cancel.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.sellerorder.buyer_request_cancel.presentation.BuyerRequestCancelRespondFragment
import dagger.Component

@BuyerRequestCancelRespondScope
@Component(
    modules = [BuyerRequestCancelRespondModule::class, BuyerRequestCancelRespondViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface BuyerRequestCancelRespondComponent {
    fun inject(buyerRequestCancelRespondFragment: BuyerRequestCancelRespondFragment)
}

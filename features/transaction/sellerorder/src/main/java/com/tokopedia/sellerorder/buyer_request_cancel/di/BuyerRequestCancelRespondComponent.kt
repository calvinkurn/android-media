package com.tokopedia.sellerorder.buyer_request_cancel.di

import com.tokopedia.sellerorder.buyer_request_cancel.presentation.BuyerRequestCancelRespondFragment
import com.tokopedia.sellerorder.common.di.SomComponent
import dagger.Component

@BuyerRequestCancelRespondScope
@Component(
    modules = [BuyerRequestCancelRespondViewModelModule::class],
    dependencies = [SomComponent::class]
)
interface BuyerRequestCancelRespondComponent {
    fun inject(buyerRequestCancelRespondFragment: BuyerRequestCancelRespondFragment)
}

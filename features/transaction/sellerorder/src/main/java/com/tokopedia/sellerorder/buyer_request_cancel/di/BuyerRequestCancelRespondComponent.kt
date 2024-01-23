package com.tokopedia.sellerorder.buyer_request_cancel.di

import com.tokopedia.sellerorder.buyer_request_cancel.presentation.BuyerRequestCancelRespondFragment
import com.tokopedia.sellerorder.common.di.SomComponent
import dagger.Component

/**
 * Created by fwidjaja on 2019-09-30.
 */

@BuyerRequestCancelRespondScope
@Component(
    modules = [BuyerRequestCancelRespondViewModelModule::class],
    dependencies = [SomComponent::class]
)
interface BuyerRequestCancelRespondComponent {
    fun inject(buyerRequestCancelRespondFragment: BuyerRequestCancelRespondFragment)
}

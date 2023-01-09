package com.tokopedia.buyerorderdetail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SubmissionOrderExtensionBottomSheet
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderExtensionFragment
import com.tokopedia.buyerorderdetail.presentation.fragment.PartialOrderFulfillmentFragment
import dagger.Component

@BuyerOrderDetailScope
@Component(
    modules = [BuyerOrderDetailModule::class, BuyerOrderDetailViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface BuyerOrderDetailComponent {
    fun inject(buyerOrderDetailFragment: BuyerOrderDetailFragment)
    fun inject(buyerOrderExtensionFragment: BuyerOrderExtensionFragment)
    fun inject(submissionOrderExtensionBottomSheet: SubmissionOrderExtensionBottomSheet)
    fun inject(partialOrderFulfillmentFragment: PartialOrderFulfillmentFragment)
}

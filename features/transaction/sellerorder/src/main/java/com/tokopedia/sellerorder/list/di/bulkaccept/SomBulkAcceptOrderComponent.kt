package com.tokopedia.sellerorder.list.di.bulkaccept

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.list.presentation.bottomsheets.SomListBulkAcceptOrderBottomSheet
import dagger.Component

@SomBulkAcceptOrderScope
@Component(modules = [SomBulkAcceptOrderViewModelModule::class], dependencies = [SomComponent::class])
interface SomBulkAcceptOrderComponent {
    fun inject(fragment: SomListBulkAcceptOrderBottomSheet)
}
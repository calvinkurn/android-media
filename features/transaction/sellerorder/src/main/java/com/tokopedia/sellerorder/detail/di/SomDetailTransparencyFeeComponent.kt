package com.tokopedia.sellerorder.detail.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomDetailTransparencyFeeBottomSheet
import dagger.Component

@SomDetailTransparencyFeeScope
@Component(
    modules = [SomDetailTransparencyFeeViewModelModule::class],
    dependencies = [SomComponent::class]
)
interface SomDetailTransparencyFeeComponent {
    fun inject(incomeDetailIncomeBottomSheet: SomDetailTransparencyFeeBottomSheet)
}

package com.tokopedia.vouchercreation.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.vouchercreation.common.di.module.VoucherCreationModule
import com.tokopedia.vouchercreation.common.di.module.VoucherCreationViewModelModule
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.CreatePromoCodeBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.MerchantVoucherTargetFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.PromotionBudgetAndTypeFragment
import com.tokopedia.vouchercreation.create.view.fragment.step.SetVoucherPeriodFragment
import com.tokopedia.vouchercreation.create.view.fragment.vouchertype.CashbackVoucherCreateFragment
import com.tokopedia.vouchercreation.create.view.fragment.vouchertype.FreeDeliveryVoucherCreateFragment
import com.tokopedia.vouchercreation.voucherlist.view.fragment.VoucherListFragment
import dagger.Component

@VoucherCreationScope
@Component(
        modules = [
            VoucherCreationModule::class,
            VoucherCreationViewModelModule::class
        ], dependencies = [BaseAppComponent::class]
)
interface VoucherCreationComponent {

    fun inject(createMerchantVoucherStepsActivity: CreateMerchantVoucherStepsActivity)
    fun inject(merchantVoucherTargetFragment: MerchantVoucherTargetFragment)
    fun inject(voucherListFragment: VoucherListFragment)
    fun inject(freeDeliveryVoucherCreateFragment: FreeDeliveryVoucherCreateFragment)
    fun inject(cashbackVoucherCreateFragment: CashbackVoucherCreateFragment)
    fun inject(promotionBudgetAndTypeFragment: PromotionBudgetAndTypeFragment)
    fun inject(createPromoCodeBottomSheetFragment: CreatePromoCodeBottomSheetFragment)
    fun inject(setVoucherPeriodFragment: SetVoucherPeriodFragment)
}
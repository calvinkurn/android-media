package com.tokopedia.vouchercreation.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.TipsTrickBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.di.module.VoucherCreationModule
import com.tokopedia.vouchercreation.common.di.module.VoucherCreationViewModelModule
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.product.list.view.activity.ProductListActivity
import com.tokopedia.vouchercreation.product.list.view.fragment.AddProductFragment
import com.tokopedia.vouchercreation.shop.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet.CreatePromoCodeBottomSheetFragment
import com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet.TipsAndTrickBottomSheetFragment
import com.tokopedia.vouchercreation.shop.create.view.fragment.step.MerchantVoucherTargetFragment
import com.tokopedia.vouchercreation.shop.create.view.fragment.step.PromotionBudgetAndTypeFragment
import com.tokopedia.vouchercreation.shop.create.view.fragment.step.ReviewVoucherFragment
import com.tokopedia.vouchercreation.shop.create.view.fragment.step.SetVoucherPeriodFragment
import com.tokopedia.vouchercreation.shop.create.view.fragment.vouchertype.CashbackVoucherCreateFragment
import com.tokopedia.vouchercreation.shop.create.view.fragment.vouchertype.FreeDeliveryVoucherCreateFragment
import com.tokopedia.vouchercreation.shop.detail.view.fragment.VoucherDetailFragment
import com.tokopedia.vouchercreation.shop.voucherlist.view.fragment.VoucherListFragment
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.EditQuotaBottomSheet
import dagger.Component

@VoucherCreationScope
@Component(
        modules = [
            VoucherCreationModule::class,
            VoucherCreationViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface VoucherCreationComponent {
    fun inject(createMerchantVoucherStepsActivity: CreateMerchantVoucherStepsActivity)
    fun inject(productListActivity: ProductListActivity)
    fun inject(merchantVoucherTargetFragment: MerchantVoucherTargetFragment)
    fun inject(voucherListFragment: VoucherListFragment)
    fun inject(addProductFragment: AddProductFragment)
    fun inject(freeDeliveryVoucherCreateFragment: FreeDeliveryVoucherCreateFragment)
    fun inject(cashbackVoucherCreateFragment: CashbackVoucherCreateFragment)
    fun inject(promotionBudgetAndTypeFragment: PromotionBudgetAndTypeFragment)
    fun inject(createPromoCodeBottomSheetFragment: CreatePromoCodeBottomSheetFragment)
    fun inject(setVoucherPeriodFragment: SetVoucherPeriodFragment)
    fun inject(reviewVoucherFragment: ReviewVoucherFragment)
    fun inject(voucherPeriodBottomSheet: VoucherPeriodBottomSheet)
    fun inject(voucherDetailFragment: VoucherDetailFragment)
    fun inject(editQuotaBottomSheet: EditQuotaBottomSheet)
    fun inject(tipsAndTrickBottomSheetFragment: TipsAndTrickBottomSheetFragment)
    fun inject(tipsTrickBottomSheet: TipsTrickBottomSheet)
}
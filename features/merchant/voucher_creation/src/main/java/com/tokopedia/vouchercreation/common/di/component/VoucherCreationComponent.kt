package com.tokopedia.vouchercreation.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.TipsTrickBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.di.module.NetworkModule
import com.tokopedia.vouchercreation.common.di.module.VoucherCreationModule
import com.tokopedia.vouchercreation.common.di.module.VoucherCreationViewModelModule
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.BroadcastCouponBottomSheet
import com.tokopedia.vouchercreation.product.preview.CouponImagePreviewBottomSheet
import com.tokopedia.vouchercreation.product.detail.view.fragment.CouponDetailFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment
import com.tokopedia.vouchercreation.product.voucherlist.view.fragment.CouponListFragment
import com.tokopedia.vouchercreation.product.preview.CouponPreviewFragment
import com.tokopedia.vouchercreation.product.detail.view.activity.VoucherProductDetailActivity
import com.tokopedia.vouchercreation.product.duplicate.DuplicateCouponActivity
import com.tokopedia.vouchercreation.product.list.view.activity.ManageProductActivity
import com.tokopedia.vouchercreation.product.list.view.activity.AddProductActivity
import com.tokopedia.vouchercreation.product.list.view.fragment.AddProductFragment
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment
import com.tokopedia.vouchercreation.product.update.UpdateCouponActivity
import com.tokopedia.vouchercreation.product.update.period.UpdateCouponPeriodBottomSheet
import com.tokopedia.vouchercreation.product.update.quota.UpdateCouponQuotaBottomSheet
import com.tokopedia.vouchercreation.product.voucherlist.view.activity.CouponListActivity
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
        VoucherCreationViewModelModule::class,
        NetworkModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface VoucherCreationComponent {
    fun inject(createMerchantVoucherStepsActivity: CreateMerchantVoucherStepsActivity)
    fun inject(addProductActivity: AddProductActivity)
    fun inject(manageProductActivity: ManageProductActivity)
    fun inject(couponListActivity: CouponListActivity)
    fun inject(updateCouponActivity: UpdateCouponActivity)
    fun inject(duplicateCouponActivity: DuplicateCouponActivity)
    fun inject(voucherProductDetailActivity: VoucherProductDetailActivity)

    fun inject(merchantVoucherTargetFragment: MerchantVoucherTargetFragment)
    fun inject(voucherListFragment: VoucherListFragment)
    fun inject(addProductFragment: AddProductFragment)
    fun inject(manageProductFragment: ManageProductFragment)
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

    fun inject(couponPreviewFragment: CouponPreviewFragment)
    fun inject(couponSettingFragment: CouponSettingFragment)
    fun inject(createCouponDetailFragment: CreateCouponDetailFragment)
    fun inject(couponListFragment: CouponListFragment)
    fun inject(couponImagePreviewBottomSheet: CouponImagePreviewBottomSheet)
    fun inject(broadcastCouponBottomSheet: BroadcastCouponBottomSheet)
    fun inject(createCouponProductActivity: CreateCouponProductActivity)
    fun inject(couponDetailFragment: CouponDetailFragment)
    fun inject(updateCouponQuotaBottomSheet: UpdateCouponQuotaBottomSheet)
    fun inject(updateCouponPeriodBottomSheet: UpdateCouponPeriodBottomSheet)
}
package com.tokopedia.vouchercreation.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.product.create.view.viewmodel.*
import com.tokopedia.vouchercreation.product.create.view.viewmodel.CouponSettingViewModel
import com.tokopedia.vouchercreation.product.create.view.viewmodel.ProductCouponPreviewViewModel
import com.tokopedia.vouchercreation.product.create.view.viewmodel.CreateCouponDetailViewModel
import com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel.CouponListViewModel
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel
import com.tokopedia.vouchercreation.shop.create.view.viewmodel.*
import com.tokopedia.vouchercreation.shop.detail.view.viewmodel.VoucherDetailViewModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewmodel.ChangeVoucherPeriodViewModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewmodel.EditQuotaViewModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewmodel.VoucherListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VoucherCreationViewModelModule {

    @VoucherCreationScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CreateMerchantVoucherStepsViewModel::class)
    internal abstract fun provideCreateMerchantVoucherStepsViewModel(createMerchantVoucherStepsViewModel: CreateMerchantVoucherStepsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MerchantVoucherTargetViewModel::class)
    internal abstract fun provideMerchantVoucherTargetViewModel(merchantVoucherTargetViewModel: MerchantVoucherTargetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherListViewModel::class)
    internal abstract fun provideVoucherListViewModel(voucherListViewModel: VoucherListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FreeDeliveryVoucherCreateViewModel::class)
    internal abstract fun provideFreeDeliveryVoucherCreateViewModel(freeDeliveryVoucherCreateViewModel: FreeDeliveryVoucherCreateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CashbackVoucherCreateViewModel::class)
    internal abstract fun provideCashbackVoucherCreateViewModel(cashbackVoucherCreateViewModel: CashbackVoucherCreateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PromotionBudgetAndTypeViewModel::class)
    internal abstract fun providePromotionBudgetAndTypeViewModel(promotionBudgetAndTypeViewModel: PromotionBudgetAndTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatePromoCodeViewModel::class)
    internal abstract fun provideCreatePromoCodeViewModel(createPromoCodeViewModel: CreatePromoCodeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetVoucherPeriodViewModel::class)
    internal abstract fun provideSetVoucherPeriodViewModel(setVoucherPeriodViewModel: SetVoucherPeriodViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewVoucherViewModel::class)
    internal abstract fun provideReviewVoucherViewModel(reviewVoucherViewModel: ReviewVoucherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeVoucherPeriodViewModel::class)
    internal abstract fun provideChangeVoucherPeriodViewModel(changeVoucherPeriodViewModel: ChangeVoucherPeriodViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherDetailViewModel::class)
    internal abstract fun provideVoucherDetailViewModel(voucherDetailViewModel: VoucherDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditQuotaViewModel::class)
    internal abstract fun provideEditQuotaViewModel(editQuotaViewModel: EditQuotaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CouponSettingViewModel::class)
    internal abstract fun provideCouponSettingViewModel(editQuotaViewModel: CouponSettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductCouponPreviewViewModel::class)
    internal abstract fun provideProductCouponPreviewViewModel(productCouponPreviewViewModel: ProductCouponPreviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateCouponDetailViewModel::class)
    internal abstract fun provideCreateCouponDetailViewModel(createCouponDetailViewModel: CreateCouponDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CouponListViewModel::class)
    internal abstract fun provideCouponListViewModel(couponListViewModel: CouponListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CouponImagePreviewViewModel::class)
    internal abstract fun provideCouponPreviewViewModel(couponPreviewViewModel: CouponImagePreviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BroadcastCouponViewModel::class)
    internal abstract fun provideBroadcastCouponViewModel(broadcastCouponViewModel: BroadcastCouponViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CouponDetailViewModel::class)
    internal abstract fun provideCouponDetailViewModel(couponDetailViewModel: CouponDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddProductViewModel::class)
    internal abstract fun provideAddProductViewModel(addProductViewModel: AddProductViewModel): ViewModel
}
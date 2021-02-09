package com.tokopedia.vouchercreation.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.create.view.viewmodel.*
import com.tokopedia.vouchercreation.detail.view.viewmodel.VoucherDetailViewModel
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.ChangeVoucherPeriodViewModel
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.EditQuotaViewModel
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.VoucherListViewModel
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
}
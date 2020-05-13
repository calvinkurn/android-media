package com.tokopedia.vouchercreation.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.create.view.viewmodel.*
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.VoucherListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@VoucherCreationScope
@Module
abstract class VoucherCreationViewModelModule {

    @Binds
    @VoucherCreationScope
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CreateMerchantVoucherStepsViewModel::class)
    abstract fun provideCreateMerchantVoucherStepsViewModel(createMerchantVoucherStepsViewModel: CreateMerchantVoucherStepsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MerchantVoucherTargetViewModel::class)
    abstract fun provideMerchantVoucherTargetViewModel(merchantVoucherTargetViewModel: MerchantVoucherTargetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherListViewModel::class)
    abstract fun provideVoucherListViewModel(voucherListViewModel: VoucherListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FreeDeliveryVoucherCreateViewModel::class)
    abstract fun provideFreeDeliveryVoucherCreateViewModel(freeDeliveryVoucherCreateViewModel: FreeDeliveryVoucherCreateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CashbackVoucherCreateViewModel::class)
    abstract fun provideCashbackVoucherCreateViewModel(cashbackVoucherCreateViewModel: CashbackVoucherCreateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PromotionBudgetAndTypeViewModel::class)
    abstract fun providePromotionBudgetAndTypeViewModel(promotionBudgetAndTypeViewModel: PromotionBudgetAndTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatePromoCodeViewModel::class)
    abstract fun provideCreatePromoCodeViewModel(createPromoCodeViewModel: CreatePromoCodeViewModel): ViewModel
}
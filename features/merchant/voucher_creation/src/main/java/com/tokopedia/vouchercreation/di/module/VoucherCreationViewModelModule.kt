package com.tokopedia.vouchercreation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.vouchercreation.create.view.viewmodel.CreateMerchantVoucherStepsViewModel
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.VoucherListViewModel
import com.tokopedia.vouchercreation.create.view.viewmodel.MerchantVoucherTargetViewModel
import com.tokopedia.vouchercreation.di.scope.VoucherCreationScope
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
}
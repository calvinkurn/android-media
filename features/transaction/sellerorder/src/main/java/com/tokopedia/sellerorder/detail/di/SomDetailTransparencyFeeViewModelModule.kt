package com.tokopedia.sellerorder.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailTransparencyFeeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SomDetailTransparencyFeeViewModelModule {
    @SomDetailTransparencyFeeScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SomDetailTransparencyFeeViewModel::class)
    internal abstract fun somDetailIncomeViewModel(viewModel: SomDetailTransparencyFeeViewModel): ViewModel
}

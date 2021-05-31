package com.tokopedia.mlp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.mlp.merchantViewModel.MerchantLendingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @MerchantScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @MerchantScope
    @ViewModelKey(MerchantLendingViewModel::class)
    internal abstract fun providesMerchantLendingViewModel(viewModel: MerchantLendingViewModel): ViewModel


}


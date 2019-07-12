package com.tokopedia.mlp.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.mlp.merchantViewModel.MerchantLendingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@MerchantScope
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


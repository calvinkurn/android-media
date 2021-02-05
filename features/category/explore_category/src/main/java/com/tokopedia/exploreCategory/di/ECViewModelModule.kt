package com.tokopedia.exploreCategory.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.exploreCategory.viewmodel.ECHomeViewModel
import com.tokopedia.exploreCategory.viewmodel.ECServiceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ECViewModelModule {

    @ECScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ECScope
    @ViewModelKey(ECServiceViewModel::class)
    internal abstract fun ecServiceViewModel(viewModel: ECServiceViewModel): ViewModel

}
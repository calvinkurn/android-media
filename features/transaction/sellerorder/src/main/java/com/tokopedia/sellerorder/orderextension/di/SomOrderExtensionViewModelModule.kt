package com.tokopedia.sellerorder.orderextension.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.orderextension.presentation.viewmodel.SomOrderExtensionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SomOrderExtensionViewModelModule {
    @SomOrderExtensionRequestScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SomOrderExtensionViewModel::class)
    internal abstract fun somOrderExtensionViewModel(viewModel: SomOrderExtensionViewModel): ViewModel
}

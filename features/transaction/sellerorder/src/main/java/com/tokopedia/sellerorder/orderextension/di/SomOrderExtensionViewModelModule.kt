package com.tokopedia.sellerorder.orderextension.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.orderextension.presentation.viewmodel.SomOrderExtensionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SomOrderExtensionViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SomOrderExtensionViewModel::class)
    internal abstract fun somOrderExtensionViewModel(viewModel: SomOrderExtensionViewModel): ViewModel
}
package com.tokopedia.attachproduct.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.attachproduct.view.viewmodel.AttachProductViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AttachProductViewModelModule {

    @Binds
    @AttachProductScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @AttachProductScope
    @ViewModelKey(AttachProductViewModel::class)
    internal abstract fun bindSettingStateViewModel(
        viewModel: AttachProductViewModel
    ): ViewModel
}
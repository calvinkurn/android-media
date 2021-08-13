package com.tokopedia.stickylogin.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.stickylogin.di.StickyLoginScope
import com.tokopedia.stickylogin.view.viewModel.StickyLoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class StickyLoginViewModelModule {

    @StickyLoginScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @StickyLoginScope
    @Binds
    @IntoMap
    @ViewModelKey(StickyLoginViewModel::class)
    abstract fun provideStickyLoginViewModel(viewModel: StickyLoginViewModel): ViewModel
}
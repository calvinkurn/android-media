package com.tokopedia.digital.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@DigitalHomePageScope
abstract class DigitalHomePageViewModelModule {


    @DigitalHomePageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalHomePageViewModel::class)
    internal abstract fun digitalHomepageViewModel(viewModel: DigitalHomePageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalHomePageSearchViewModel::class)
    internal abstract fun digitalHomepageSearchViewModel(viewModel: DigitalHomePageSearchViewModel): ViewModel
}
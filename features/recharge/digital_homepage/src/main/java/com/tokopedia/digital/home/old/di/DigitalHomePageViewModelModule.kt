package com.tokopedia.digital.home.old.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital.home.old.presentation.viewmodel.DigitalHomePageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DigitalHomePageViewModelModule {

    @DigitalHomePageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalHomePageViewModel::class)
    internal abstract fun digitalHomepageViewModel(viewModel: DigitalHomePageViewModel): ViewModel

}
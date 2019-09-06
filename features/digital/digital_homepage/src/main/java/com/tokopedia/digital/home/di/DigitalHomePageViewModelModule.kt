package com.tokopedia.digital.home.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Module
@DigitalHomePageScope
abstract class DigitalHomePageViewModelModule {


    @DigitalHomePageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalHomePageViewModel::class)
    internal abstract fun travelHomepageViewModel(viewModel: DigitalHomePageViewModel): ViewModel
}
package com.tokopedia.searchbar.navigation_component.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.searchbar.navigation_component.di.NavigationScope
import com.tokopedia.searchbar.navigation_component.viewModel.NavigationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NavigationViewModelModule {
    @NavigationScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @NavigationScope
    @Binds
    @IntoMap
    @ViewModelKey(NavigationViewModel::class)
    abstract fun provideStickyLoginViewModel(viewModel: NavigationViewModel): ViewModel
}
package com.tokopedia.tokopedianow.home.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.home.di.scope.HomeScope
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {

    @Binds
    @HomeScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowHomeViewModel::class)
    internal abstract fun tokoMartHomeViewModel(viewModelTokoNow: TokoNowHomeViewModel): ViewModel
}
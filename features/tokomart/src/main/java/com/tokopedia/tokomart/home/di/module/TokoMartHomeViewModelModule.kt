package com.tokopedia.tokomart.home.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokomart.home.di.scope.TokoMartHomeScope
import com.tokopedia.tokomart.home.presentation.viewmodel.TokoMartHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoMartHomeViewModelModule {

    @Binds
    @TokoMartHomeScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoMartHomeViewModel::class)
    internal abstract fun tokoMartHomeViewModel(viewModel: TokoMartHomeViewModel): ViewModel
}
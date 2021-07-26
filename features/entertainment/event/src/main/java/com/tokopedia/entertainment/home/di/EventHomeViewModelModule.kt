package com.tokopedia.entertainment.home.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.entertainment.home.viewmodel.EventHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 06,February,2020
 */

@Module
abstract class EventHomeViewModelModule {

    @Binds
    @EventHomeScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @EventHomeScope
    @ViewModelKey(EventHomeViewModel::class)
    internal abstract fun provideHomeViewModel(viewModel: EventHomeViewModel): ViewModel

}
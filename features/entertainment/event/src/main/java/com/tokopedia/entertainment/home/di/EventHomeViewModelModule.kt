package com.tokopedia.entertainment.home.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.entertainment.home.viewmodel.HomeEventViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 06,February,2020
 */

@Module
abstract class EventHomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeEventViewModel::class)
    internal abstract fun provideHomeViewModel(viewModel: HomeEventViewModel): ViewModel

}
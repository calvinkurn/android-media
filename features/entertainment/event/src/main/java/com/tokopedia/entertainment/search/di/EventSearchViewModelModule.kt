package com.tokopedia.entertainment.search.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.entertainment.search.viewmodel.EventSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 06,February,2020
 */

@Module
abstract class EventSearchViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(EventSearchViewModel::class)
    internal abstract fun provideHomeViewModel(viewModel: EventSearchViewModel): ViewModel

}
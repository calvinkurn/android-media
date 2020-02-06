package com.tokopedia.entertainment.home.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.entertainment.home.viewmodel.HomeEntertainmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 06,February,2020
 */

@EntHomeScope
@Module
abstract class EntHomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeEntertainmentViewModel::class)
    internal abstract fun provideHomeViewModel(viewModel: HomeEntertainmentViewModel): ViewModel

}
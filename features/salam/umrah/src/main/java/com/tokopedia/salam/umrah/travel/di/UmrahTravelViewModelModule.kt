package com.tokopedia.salam.umrah.travel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by Firman on 22/1/20
 */

@Module
abstract class UmrahTravelViewModelModule {

    @UmrahTravelScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UmrahTravelViewModel::class)
    internal abstract fun umrahTravelViewModel(viewModel: UmrahTravelViewModel): ViewModel
}
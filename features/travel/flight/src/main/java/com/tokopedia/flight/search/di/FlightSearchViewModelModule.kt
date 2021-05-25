package com.tokopedia.flight.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.search.presentation.viewmodel.FlightSearchReturnViewModel
import com.tokopedia.flight.search.presentation.viewmodel.FlightSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 06/04/2020
 */

@Module
abstract class FlightSearchViewModelModule {
    @FlightSearchScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @FlightSearchScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightSearchViewModel::class)
    abstract fun flightSearchViewModel(viewModel: FlightSearchViewModel): ViewModel

    @FlightSearchScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightSearchReturnViewModel::class)
    abstract fun flightSearchReturnViewModel(viewModel: FlightSearchReturnViewModel): ViewModel

}
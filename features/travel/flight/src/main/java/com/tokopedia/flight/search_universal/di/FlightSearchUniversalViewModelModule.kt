package com.tokopedia.flight.search_universal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.search_universal.presentation.viewmodel.FlightSearchUniversalViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 10/03/2020
 */
@Module
abstract class FlightSearchUniversalViewModelModule {

    @FlightSearchUniversalScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightSearchUniversalViewModel::class)
    internal abstract fun flightSearchUniversalViewModel(viewModel: FlightSearchUniversalViewModel): ViewModel

}
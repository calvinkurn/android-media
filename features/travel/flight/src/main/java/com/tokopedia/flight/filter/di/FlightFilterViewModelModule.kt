package com.tokopedia.flight.filter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.filter.presentation.viewmodel.FlightFilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 17/02/2020
 */
@Module
abstract class FlightFilterViewModelModule {

    @FlightFilterScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FlightFilterViewModel::class)
    internal abstract fun flightFilterViewModel(viewModel: FlightFilterViewModel): ViewModel

}
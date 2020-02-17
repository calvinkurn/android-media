package com.tokopedia.flight.filter.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * @author by furqan on 17/02/2020
 */
@FlightFilterScope
@Module
abstract class FlightFilterViewModelModule {
    @FlightFilterScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
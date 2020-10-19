package com.tokopedia.flight.orderdetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.orderdetail.presentation.viewmodel.FlightOrderDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 19/10/2020
 */
@FlightOrderDetailScope
@Module
abstract class FlightOrderDetailViewModelModule {

    @FlightOrderDetailScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @FlightOrderDetailScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightOrderDetailViewModel::class)
    abstract fun flightOrderDetailViewModel(viewModel: FlightOrderDetailViewModel): ViewModel
}
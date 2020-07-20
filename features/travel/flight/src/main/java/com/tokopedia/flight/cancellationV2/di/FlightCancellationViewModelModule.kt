package com.tokopedia.flight.cancellationV2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.cancellationV2.presentation.viewmodel.FlightCancellationChooseReasonViewModel
import com.tokopedia.flight.cancellationV2.presentation.viewmodel.FlightCancellationPassengerViewModel
import com.tokopedia.flight.cancellationV2.presentation.viewmodel.FlightCancellationReasonViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 06/07/2020
 */
@FlightCancellationScope
@Module
abstract class FlightCancellationViewModelModule {

    @FlightCancellationScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @FlightCancellationScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightCancellationPassengerViewModel::class)
    abstract fun flightCancellationPassengerViewModel(viewModel: FlightCancellationPassengerViewModel): ViewModel

    @FlightCancellationScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightCancellationReasonViewModel::class)
    abstract fun flightCancellationReasonViewModel(viewModel: FlightCancellationReasonViewModel): ViewModel

    @FlightCancellationScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightCancellationChooseReasonViewModel::class)
    abstract fun flightCancellationChooseReasonViewModel(viewModel: FlightCancellationChooseReasonViewModel): ViewModel

}
package com.tokopedia.flight.cancellation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationChooseReasonViewModel
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationPassengerViewModel
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationReasonViewModel
import com.tokopedia.flight.cancellation.presentation.viewmodel.FlightCancellationReviewViewModel
import com.tokopedia.flight.cancellation_navigation.presentation.viewmodel.FlightCancellationBottomSheetChooseReasonViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 06/07/2020
 */
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

    @FlightCancellationScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightCancellationBottomSheetChooseReasonViewModel::class)
    abstract fun flightCancellationBottomSheetChooseReasonViewModel(viewModel: FlightCancellationBottomSheetChooseReasonViewModel): ViewModel

    @FlightCancellationScope
    @Binds
    @IntoMap
    @ViewModelKey(FlightCancellationReviewViewModel::class)
    abstract fun flightCancellationReviewViewModel(viewModel: FlightCancellationReviewViewModel): ViewModel

}
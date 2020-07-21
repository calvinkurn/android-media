package com.tokopedia.flight.cancellationV2.di

import com.tokopedia.flight.cancellationV2.presentation.fragment.FlightCancellationChooseReasonFragment
import com.tokopedia.flight.cancellationV2.presentation.fragment.FlightCancellationPassengerFragment
import com.tokopedia.flight.cancellationV2.presentation.fragment.FlightCancellationReasonFragment
import com.tokopedia.flight.cancellationV2.presentation.fragment.FlightCancellationReviewFragment
import com.tokopedia.flight.common.di.component.FlightComponent
import dagger.Component

/**
 * @author by furqan on 06/07/2020
 */
@FlightCancellationScope
@Component(modules = [FlightCancellationModule::class, FlightCancellationViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightCancellationComponent {
    fun inject(flightCancellationPassengerFragment: FlightCancellationPassengerFragment)
    fun inject(flightCancellationReasonFragment: FlightCancellationReasonFragment)
    fun inject(flightCancellationChooseReasonFragment: FlightCancellationChooseReasonFragment)
    fun inject(flightCancellationReviewFragment: FlightCancellationReviewFragment)
}
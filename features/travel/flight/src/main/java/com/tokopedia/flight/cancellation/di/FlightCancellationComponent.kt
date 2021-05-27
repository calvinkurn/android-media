package com.tokopedia.flight.cancellation.di

import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationChooseReasonFragment
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationPassengerFragment
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationReasonFragment
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationReviewFragment
import com.tokopedia.flight.cancellation_navigation.presentation.bottomsheet.FlightCancellationChooseReasonBottomSheet
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

    // Inject for cancellation navigation fragment
    fun inject(flightCancellationPassengerFragment: com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationPassengerFragment)
    fun inject(flightCancellationReasonFragment: com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationReasonFragment)
    fun inject(flightCancellationChooseReasonBottomSheet: FlightCancellationChooseReasonBottomSheet)
}
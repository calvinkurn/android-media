package com.tokopedia.flight.cancellationV2.di

import com.tokopedia.flight.common.di.component.FlightComponent
import dagger.Component

/**
 * @author by furqan on 06/07/2020
 */
@FlightCancellationScope
@Component(modules = [FlightCancellationModule::class, FlightCancellationViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightCancellationComponent {

}
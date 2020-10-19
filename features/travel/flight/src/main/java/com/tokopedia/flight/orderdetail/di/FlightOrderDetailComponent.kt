package com.tokopedia.flight.orderdetail.di

import com.tokopedia.flight.common.di.component.FlightComponent
import dagger.Component

/**
 * @author by furqan on 19/10/2020
 */
@FlightOrderDetailScope
@Component(modules = [FlightOrderDetailModule::class, FlightOrderDetailViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightOrderDetailComponent {
}
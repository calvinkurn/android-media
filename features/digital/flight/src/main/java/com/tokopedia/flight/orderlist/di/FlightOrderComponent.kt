package com.tokopedia.flight.orderlist.di

import com.tokopedia.flight.FlightModuleRouter
import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.detail.view.fragment.FlightDetailOrderFragment
import com.tokopedia.flight.orderlist.view.FlightOrderListFragment
import com.tokopedia.flight.orderlist.view.fragment.FlightResendETicketDialogFragment

import dagger.Component

/**
 * Created by alvarisi on 12/6/17.
 */
@FlightOrderScope
@Component(modules = [FlightOrderModule::class], dependencies = [FlightComponent::class])
interface FlightOrderComponent {

    fun flightModuleRouter(): FlightModuleRouter

    fun inject(flightOrderListFragment: FlightOrderListFragment)

    fun inject(flightDetailOrderFragment: FlightDetailOrderFragment)

    fun inject(flightResendETicketDialogFragment: FlightResendETicketDialogFragment)
}

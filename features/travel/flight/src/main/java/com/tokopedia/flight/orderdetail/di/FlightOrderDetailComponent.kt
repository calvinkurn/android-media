package com.tokopedia.flight.orderdetail.di

import com.tokopedia.flight.cancellationdetail.presentation.fragment.FlightOrderCancellationDetailFragment
import com.tokopedia.flight.cancellationdetail.presentation.fragment.FlightOrderCancellationListFragment
import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.orderdetail.presentation.activity.FlightOrderDetailActivity
import com.tokopedia.flight.orderdetail.presentation.fragment.FlightOrderDetailBrowserFragment
import com.tokopedia.flight.orderdetail.presentation.fragment.FlightOrderDetailFragment
import com.tokopedia.flight.orderdetail.presentation.fragment.FlightOrderDetailWebCheckInFragment
import dagger.Component

/**
 * @author by furqan on 19/10/2020
 */
@FlightOrderDetailScope
@Component(modules = [FlightOrderDetailModule::class, FlightOrderDetailViewModelModule::class],
        dependencies = [FlightComponent::class])
interface FlightOrderDetailComponent {
    fun inject(flightOrderDetailActivity: FlightOrderDetailActivity)
    fun inject(flightOrderDetailActivity: FlightOrderDetailFragment)
    fun inject(flightOrderDetailWebCheckInFragment: FlightOrderDetailWebCheckInFragment)
    fun inject(flightOrderDetailBrowserFragment: FlightOrderDetailBrowserFragment)
    fun inject(flightOrderCancellationListFragment: FlightOrderCancellationListFragment)
    fun inject(flightOrderCancellationDetailFragment: FlightOrderCancellationDetailFragment)
}
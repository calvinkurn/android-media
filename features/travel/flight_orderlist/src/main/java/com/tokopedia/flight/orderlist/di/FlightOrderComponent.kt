package com.tokopedia.flight.orderlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.orderlist.data.FlightOrderApi
import com.tokopedia.flight.orderlist.view.fragment.FlightOrderListFragment
import com.tokopedia.flight.resend_email.presentation.bottomsheet.FlightOrderResendEmailBottomSheet
import dagger.Component

/**
 * Created by alvarisi on 12/6/17.
 */
@FlightOrderScope
@Component(modules = [FlightOrderModule::class, FlightOrderViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface FlightOrderComponent {

    @ApplicationContext
    fun context(): Context

    fun flightOrderApi(): FlightOrderApi

    fun inject(flightOrderListFragment: FlightOrderListFragment)

    fun inject(flightOrderResendEmailBottomSheet: FlightOrderResendEmailBottomSheet)

}

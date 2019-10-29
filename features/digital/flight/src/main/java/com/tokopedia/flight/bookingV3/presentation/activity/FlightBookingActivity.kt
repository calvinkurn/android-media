package com.tokopedia.flight.bookingV3.presentation.activity

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.bookingV3.presentation.fragment.FlightBookingFragment
import com.tokopedia.flight.common.view.BaseFlightActivity

/**
 * @author by jessica on 2019-10-23
 */

class FlightBookingActivity: BaseFlightActivity(), HasComponent<FlightBookingComponent> {
    override fun getNewFragment(): Fragment = FlightBookingFragment()

    override fun getComponent(): FlightBookingComponent? {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(flightComponent)
                .build()
    }

    companion object {
        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, FlightBookingActivity::class.java)
            return intent
        }
    }

}
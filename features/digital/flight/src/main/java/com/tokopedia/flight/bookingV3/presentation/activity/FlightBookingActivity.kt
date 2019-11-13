package com.tokopedia.flight.bookingV3.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.bookingV3.presentation.fragment.FlightBookingFragment
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by jessica on 2019-10-23
 */

class FlightBookingActivity: BaseFlightActivity(), HasComponent<FlightBookingComponent> {

    lateinit var userSession: UserSessionInterface
        @Inject set

    override fun getNewFragment(): Fragment = FlightBookingFragment()

    override fun getComponent(): FlightBookingComponent {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(flightComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        if (!userSession.isLoggedIn) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_LOGIN -> if (userSession.isLoggedIn) recreate() else finish()
        }
    }

    companion object {
        val REQUEST_CODE_LOGIN = 6

        fun getCallingIntent(activity: Activity): Intent {
            val intent = Intent(activity, FlightBookingActivity::class.java)
            return intent
        }
    }

}
package com.tokopedia.flight.dashboardV2.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.dashboardV2.di.DaggerFlightDashboardV2Component
import com.tokopedia.flight.dashboardV2.di.FlightDashboardV2Component
import com.tokopedia.flight.dashboardV2.presentation.fragment.FlightDashboardV2Fragment

class FlightDashboardV2Activity : BaseFlightActivity(), HasComponent<FlightDashboardV2Component> {

    override fun getNewFragment(): Fragment? =
            FlightDashboardV2Fragment.getInstance("")

    override fun getComponent(): FlightDashboardV2Component =
            DaggerFlightDashboardV2Component.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    companion object {
        private const val EXTRA_TRIP = "dest"
        private const val EXTRA_ADULT = "a"
        private const val EXTRA_CHILD = "c"
        private const val EXTRA_INFANT = "i"
        private const val EXTRA_CLASS = "s"
        private const val EXTRA_AUTO_SEARCH = "auto_search"

        fun getCallingIntent(context: Context): Intent =
                Intent(context, FlightDashboardV2Activity::class.java)

    }
}

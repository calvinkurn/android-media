package com.tokopedia.flight.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.common.constant_kotlin.FlightUrl.FLIGHT_HOMEPAGE_HELP_URL
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.homepage.di.DaggerFlightHomepageComponent
import com.tokopedia.flight.homepage.di.FlightHomepageComponent
import com.tokopedia.flight.homepage.presentation.fragment.FlightHomepageFragment

class FlightHomepageActivity : BaseFlightActivity(), HasComponent<FlightHomepageComponent> {

    private var extrasTrip = ""
    private var extrasAdult = ""
    private var extrasChild = ""
    private var extrasInfant = ""
    private var extrasClass = ""
    private var extrasAutoSearch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        intent.data?.let {
            if (!it.getQueryParameter(EXTRA_TRIP).isNullOrEmpty() &&
                    !it.getQueryParameter(EXTRA_ADULT).isNullOrEmpty() &&
                    !it.getQueryParameter(EXTRA_CHILD).isNullOrEmpty() &&
                    !it.getQueryParameter(EXTRA_INFANT).isNullOrEmpty() &&
                    !it.getQueryParameter(EXTRA_CLASS).isNullOrEmpty()) {
                extrasTrip = it.getQueryParameter(EXTRA_TRIP) ?: ""
                extrasAdult = it.getQueryParameter(EXTRA_ADULT) ?: ""
                extrasChild = it.getQueryParameter(EXTRA_CHILD) ?: ""
                extrasInfant = it.getQueryParameter(EXTRA_INFANT) ?: ""
                extrasClass = it.getQueryParameter(EXTRA_CLASS) ?: ""
                extrasAutoSearch = if (!it.getQueryParameter(EXTRA_AUTO_SEARCH).isNullOrEmpty())
                    it.getQueryParameter(EXTRA_AUTO_SEARCH) ?: "0" else "0"
            }
        }

        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment =
            if (extrasTrip.isNotEmpty() && extrasAdult.isNotEmpty() && extrasChild.isNotEmpty() &&
                    extrasInfant.isNotEmpty() && extrasClass.isNotEmpty() && extrasAutoSearch.isNotEmpty()) {
                FlightHomepageFragment.getInstance(
                        extrasTrip,
                        extrasAdult,
                        extrasChild,
                        extrasInfant,
                        extrasClass,
                        extrasAutoSearch,
                        intent.data?.toString() ?: ""
                )
            } else {
                FlightHomepageFragment.getInstance(intent.data?.toString() ?: "")
            }

    override fun getComponent(): FlightHomepageComponent =
            DaggerFlightHomepageComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    override fun navigateToHelpPage() {
        RouteManager.route(this, FLIGHT_HOMEPAGE_HELP_URL)
    }

    companion object {
        private const val EXTRA_TRIP = "dest"
        private const val EXTRA_ADULT = "a"
        private const val EXTRA_CHILD = "c"
        private const val EXTRA_INFANT = "i"
        private const val EXTRA_CLASS = "s"
        private const val EXTRA_AUTO_SEARCH = "auto_search"

        fun getCallingIntent(context: Context): Intent =
                Intent(context, FlightHomepageActivity::class.java)

    }
}

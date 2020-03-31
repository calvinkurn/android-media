package com.tokopedia.flight.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.homepage.di.DaggerFlightHomepageComponent
import com.tokopedia.flight.homepage.di.FlightHomepageComponent
import com.tokopedia.flight.homepage.presentation.fragment.FlightHomepageFragment

class FlightHomepageActivity : BaseFlightActivity(), HasComponent<FlightHomepageComponent> {

    override fun getNewFragment(): Fragment =
            if (intent.hasExtra(EXTRA_TRIP) &&
                    intent.hasExtra(EXTRA_ADULT) &&
                    intent.hasExtra(EXTRA_CHILD) &&
                    intent.hasExtra(EXTRA_INFANT) &&
                    intent.hasExtra(EXTRA_CLASS)) {
                FlightHomepageFragment.getInstance(
                        intent.getStringExtra(EXTRA_TRIP),
                        intent.getStringExtra(EXTRA_ADULT),
                        intent.getStringExtra(EXTRA_CHILD),
                        intent.getStringExtra(EXTRA_INFANT),
                        intent.getStringExtra(EXTRA_CLASS),
                        if (intent.hasExtra(EXTRA_AUTO_SEARCH)) intent.getStringExtra(EXTRA_AUTO_SEARCH)
                        else "0",
                        intent.data?.toString() ?: ""
                )
            } else {
                FlightHomepageFragment.getInstance(intent.data?.toString() ?: "")
            }

    override fun getComponent(): FlightHomepageComponent =
            DaggerFlightHomepageComponent.builder()
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
                Intent(context, FlightHomepageActivity::class.java)

    }
}

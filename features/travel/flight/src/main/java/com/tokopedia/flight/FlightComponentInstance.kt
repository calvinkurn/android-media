package com.tokopedia.flight

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.flight.common.di.component.DaggerFlightComponent
import com.tokopedia.flight.common.di.component.FlightComponent

/**
 * Created by nakama on 11/12/17.
 */
object FlightComponentInstance {
    private var flightComponent: FlightComponent? = null

    @JvmStatic
    fun getFlightComponent(application: Application): FlightComponent? {
        if (flightComponent == null) {
            flightComponent = DaggerFlightComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
        return flightComponent
    }
}
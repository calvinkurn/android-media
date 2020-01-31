package com.tokopedia.travel.passenger.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * @author by furqan on 03/01/2020
 */
object TravelPassengerComponentUtils {
    private lateinit var travelCountryCodeComponent: TravelPassengerComponent

    fun getTravelPassengerComponent(application: Application): TravelPassengerComponent {
        if (!::travelCountryCodeComponent.isInitialized) {
            travelCountryCodeComponent = DaggerTravelPassengerComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return travelCountryCodeComponent
    }
}
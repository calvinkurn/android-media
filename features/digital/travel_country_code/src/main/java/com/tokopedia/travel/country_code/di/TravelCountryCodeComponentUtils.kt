package com.tokopedia.travel.country_code.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * @author by furqan on 23/12/2019
 */
object TravelCountryCodeComponentUtils {

    private lateinit var travelCountryCodeComponent: TravelCountryCodeComponent

    fun getTravelCountryCodeComponent(application: Application): TravelCountryCodeComponent {
        if (!::travelCountryCodeComponent.isInitialized) {
            travelCountryCodeComponent = DaggerTravelCountryCodeComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return travelCountryCodeComponent
    }

}
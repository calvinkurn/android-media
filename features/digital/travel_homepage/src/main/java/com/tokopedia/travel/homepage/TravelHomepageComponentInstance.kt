package com.tokopedia.travel.homepage

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.travel.homepage.di.DaggerTravelHomepageComponent
import com.tokopedia.travel.homepage.di.TravelHomepageComponent

/**
 * @author by furqan on 05/08/2019
 */
object TravelHomepageComponentInstance {
    private lateinit var travelHomepageComponent: TravelHomepageComponent

    fun getTravelHomepageComponent(application: Application): TravelHomepageComponent {
        if (!::travelHomepageComponent.isInitialized) {
            travelHomepageComponent =  DaggerTravelHomepageComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return travelHomepageComponent
    }
}
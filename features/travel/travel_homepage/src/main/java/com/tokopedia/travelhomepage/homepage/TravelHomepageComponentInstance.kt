package com.tokopedia.travelhomepage.homepage

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.travelhomepage.homepage.di.DaggerTravelHomepageComponent
import com.tokopedia.travelhomepage.homepage.di.TravelHomepageComponent

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
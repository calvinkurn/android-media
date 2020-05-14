package com.tokopedia.travelhomepage.destination

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.travelhomepage.destination.di.DaggerTravelDestinationComponent
import com.tokopedia.travelhomepage.destination.di.TravelDestinationComponent

/**
 * @author by jessica on 2019-12-30
 */

object TravelDestinationComponentInstance  {
    private lateinit var travelDestinationComponent: TravelDestinationComponent

    fun getTravelDestinationComponent(application: Application): TravelDestinationComponent {
        if (!::travelDestinationComponent.isInitialized) {
            travelDestinationComponent =  DaggerTravelDestinationComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return travelDestinationComponent
    }
}
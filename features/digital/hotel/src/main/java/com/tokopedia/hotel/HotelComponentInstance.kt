package com.tokopedia.hotel

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.common.di.component.DaggerHotelComponent

/**
 * @author by furqan on 25/03/19
 */
object HotelComponentInstance {
    private var digitalBrowseComponent: HotelComponent? = null

    fun getDigitalBrowseComponent(application: Application): HotelComponent {
        if (digitalBrowseComponent == null) {
            digitalBrowseComponent = DaggerHotelComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return digitalBrowseComponent as HotelComponent
    }
}
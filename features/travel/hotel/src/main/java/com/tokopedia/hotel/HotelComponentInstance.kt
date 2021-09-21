package com.tokopedia.hotel

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.hotel.common.di.component.DaggerHotelComponent
import com.tokopedia.hotel.common.di.component.HotelComponent

/**
 * @author by furqan on 25/03/19
 */
object HotelComponentInstance {
    private lateinit var hotelComponent: HotelComponent

    fun getHotelComponent(application: Application): HotelComponent {
        if (!::hotelComponent.isInitialized) {
            hotelComponent = DaggerHotelComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return hotelComponent
    }
}
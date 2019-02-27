package com.tokopedia.travelcalendar.view

import android.app.Application

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.travelcalendar.di.DaggerTravelCalendarComponent
import com.tokopedia.travelcalendar.di.TravelCalendarComponent

/**
 * Created by nabillasabbaha on 14/05/18.
 */
object TravelCalendarComponentInstance {

    fun getComponent(application: Application): TravelCalendarComponent {
        val travelCalendarComponent: TravelCalendarComponent by lazy {
            DaggerTravelCalendarComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }
        return travelCalendarComponent
    }
}

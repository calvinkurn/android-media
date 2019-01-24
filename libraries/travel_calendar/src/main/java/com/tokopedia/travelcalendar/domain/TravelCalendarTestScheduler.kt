package com.tokopedia.travelcalendar.domain

import rx.Scheduler
import rx.schedulers.Schedulers

/**
 * Created by nabillasabbaha on 10/08/18.
 */
class TravelCalendarTestScheduler : TravelCalendarProvider {

    override fun computation(): Scheduler {
        return Schedulers.immediate()
    }

    override fun uiScheduler(): Scheduler {
        return Schedulers.immediate()
    }
}

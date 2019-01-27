package com.tokopedia.travelcalendar.domain

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by nabillasabbaha on 10/08/18.
 */
class TravelCalendarScheduler : TravelCalendarProvider {

    override fun computation(): Scheduler {
        return Schedulers.io()
    }

    override fun uiScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}

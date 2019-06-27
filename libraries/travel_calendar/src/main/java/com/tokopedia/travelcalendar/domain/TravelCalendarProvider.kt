package com.tokopedia.travelcalendar.domain

import rx.Scheduler

/**
 * Created by nabillasabbaha on 10/08/18.
 */
interface TravelCalendarProvider {

    fun computation(): Scheduler

    fun uiScheduler(): Scheduler
}

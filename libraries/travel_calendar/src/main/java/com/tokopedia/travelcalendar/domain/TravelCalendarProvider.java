package com.tokopedia.travelcalendar.domain;

import rx.Scheduler;

/**
 * Created by nabillasabbaha on 10/08/18.
 */
public interface TravelCalendarProvider {

    Scheduler computation();

    Scheduler uiScheduler();
}

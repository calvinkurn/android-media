package com.tokopedia.travelcalendar.domain;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by nabillasabbaha on 10/08/18.
 */
public class TravelCalendarTestScheduler implements TravelCalendarProvider {

    public TravelCalendarTestScheduler() {
    }

    @Override
    public Scheduler computation() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler uiScheduler() {
        return Schedulers.immediate();
    }
}

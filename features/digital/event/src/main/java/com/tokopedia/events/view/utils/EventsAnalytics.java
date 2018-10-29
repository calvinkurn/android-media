package com.tokopedia.events.view.utils;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

public class EventsAnalytics {

    private static String EVENT_DIGITAL_EVENT = "digitalGeneralEvent";
    private static String DIGITAL_EVENT = "digital - event";

    private AnalyticTracker tracker;


    @Inject
    public EventsAnalytics(@ApplicationContext Context context) {
        if (context != null && context.getApplicationContext() instanceof AbstractionRouter) {
            tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        }
    }

    public void eventDigitalEventTracking(String action, String label) {
        Log.d("EVENTSGA", action + " - " + label);
        if (tracker == null)
            return;
        tracker.sendEventTracking(EVENT_DIGITAL_EVENT, DIGITAL_EVENT, action, label);
    }
}

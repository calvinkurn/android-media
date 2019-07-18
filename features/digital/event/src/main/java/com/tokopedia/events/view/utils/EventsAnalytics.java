package com.tokopedia.events.view.utils;

import android.util.Log;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import javax.inject.Inject;

public class EventsAnalytics {

    private static String EVENT_DIGITAL_EVENT = "digitalGeneralEvent";
    private static String DIGITAL_EVENT = "digital - event";



    @Inject
    public EventsAnalytics() {
    }

    public void eventDigitalEventTracking(String action, String label) {
        Log.d("EVENTSGA", action + " - " + label);
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_DIGITAL_EVENT, DIGITAL_EVENT, action, label));
    }
}

package com.tokopedia.tkpd.analytics.nishikino.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by alvarisi on 6/29/2016.
 */
public class EventTracking {
    private Map<String, Object> eventTracking = new HashMap<>();

    public EventTracking() {
    }

    public EventTracking(String event, String category, String action, String label) {
        this.eventTracking.put("event", event);
        this.eventTracking.put("eventCategory", category);
        this.eventTracking.put("eventAction", action);
        this.eventTracking.put("eventLabel", label);
    }

    public void setEvent(String event) {

        eventTracking.put("event", event);
    }

    public void setEventCategory(String eventCategory) {
        eventTracking.put("eventCategory", eventCategory);
    }

    public void setEventAction(String eventAction) {
        eventTracking.put("eventAction", eventAction);
    }

    public void setEventLabel(String eventLabel) {
        eventTracking.put("eventLabel", eventLabel);
    }

    public Map<String, Object> getEvent() {
        return this.eventTracking;
    }
}
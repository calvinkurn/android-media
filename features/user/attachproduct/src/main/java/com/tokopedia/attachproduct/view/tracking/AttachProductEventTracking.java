package com.tokopedia.attachproduct.view.tracking;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hendri on 08/08/18.
 */
public class AttachProductEventTracking {
    protected Map<String, Object> eventTracking = new HashMap<>();

    public AttachProductEventTracking() {
    }

    public AttachProductEventTracking(String event, String category, String action, String label) {
        Log.d("GAv4", "EventTracking: " + event + " " + category + " " + action + " " + label
                + " ");
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

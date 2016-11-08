package com.tokopedia.core.analytics.nishikino.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by Alvarisi on 6/15/2016.
 * Unused change to EventTracking
 */
public class ButtonClickEvent {
    private Map<String, Object> buttonEventMap = new HashMap<>();

    public ButtonClickEvent() {
    }

    public ButtonClickEvent(String event, String category, String action, String label) {
        this.buttonEventMap.put("event", event);
        this.buttonEventMap.put("eventCategory", category);
        this.buttonEventMap.put("eventAction", action);
        this.buttonEventMap.put("eventLabel", label);
    }

    public void setEvent(String event) {
        this.buttonEventMap.put("event", event);
    }

    public void setEventCategory(String eventCategory) {
        this.buttonEventMap.put("eventCategory", eventCategory);
    }

    public void setEventAction(String eventAction) {
        this.buttonEventMap.put("eventAction", eventAction);
    }

    public void setEventLabel(String eventLabel) {
        this.buttonEventMap.put("eventLabel", eventLabel);
    }

    public Map<String, Object> getEvent() {
        return this.buttonEventMap;
    }
}

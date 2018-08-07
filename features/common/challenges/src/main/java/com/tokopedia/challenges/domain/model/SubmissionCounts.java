
package com.tokopedia.challenges.domain.model;

import java.util.HashMap;
import java.util.Map;

public class SubmissionCounts {

    private Integer approved;
    private Integer waiting;
    private Integer declined;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getWaiting() {
        return waiting;
    }

    public void setWaiting(Integer waiting) {
        this.waiting = waiting;
    }

    public Integer getDeclined() {
        return declined;
    }

    public void setDeclined(Integer declined) {
        this.declined = declined;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

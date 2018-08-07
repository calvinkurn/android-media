
package com.tokopedia.challenges.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Challenge {

    private Integer start;
    private Integer size;
    private Integer found;
    private Integer count;
    private List<Result> results = null;
    private Channel_ channel;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Channel_ getChannel() {
        return channel;
    }

    public void setChannel(Channel_ channel) {
        this.channel = channel;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

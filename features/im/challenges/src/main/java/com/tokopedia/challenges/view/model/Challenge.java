
package com.tokopedia.challenges.view.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Challenge {

    @SerializedName("Start")
    @Expose
    private Integer start;
    @SerializedName("Size")
    @Expose
    private Integer size;
    @SerializedName("Found")
    @Expose
    private Integer found;
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Results")
    @Expose
    private List<Result> results = null;
    @SerializedName("Channel")
    @Expose
    private Channel_ channel;

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

}

package com.tokopedia.flight.cancellation.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 29/10/18.
 */

public class ReasonAttributes {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("required_docs")
    @Expose
    private List<String> requiredDocs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getRequiredDocs() {
        return requiredDocs;
    }

    public void setRequiredDocs(List<String> requiredDocs) {
        this.requiredDocs = requiredDocs;
    }
}

package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 02/07/18.
 */

public class GuideAttributeEntity {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("source_link")
    @Expose
    private String sourceLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }
}

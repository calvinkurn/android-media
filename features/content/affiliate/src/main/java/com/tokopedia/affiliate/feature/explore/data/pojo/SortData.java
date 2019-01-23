package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 15/01/19.
 */
public class SortData {

    @SerializedName("key")
    private String key;
    @SerializedName("asc")
    private boolean asc;
    @SerializedName("text")
    private String text;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

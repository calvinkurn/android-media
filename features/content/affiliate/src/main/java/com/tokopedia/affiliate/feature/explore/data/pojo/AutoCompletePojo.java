package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 24/10/18.
 */
public class AutoCompletePojo {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("formatted")
    @Expose
    private String formatted;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }
}

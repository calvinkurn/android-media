package com.tokopedia.tokocash.ovoactivation.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 01/10/18.
 */
public class PhoneActionEntity {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("Applink")
    @Expose
    private String applink;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public String getApplink() {
        return applink;
    }
}

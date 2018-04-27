package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class CrackButtonEntity {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("applink")
    @Expose
    private String applink;

    @SerializedName("type")
    @Expose
    private String type;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getApplink() {
        return applink;
    }

    public String getType() {
        return type;
    }
}

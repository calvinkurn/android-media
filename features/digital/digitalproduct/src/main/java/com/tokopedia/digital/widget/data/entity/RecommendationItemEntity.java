package com.tokopedia.digital.widget.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 14/11/18.
 */
public class RecommendationItemEntity {

    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("clientNumber")
    @Expose
    private String clientNumber;

    @SerializedName("applink")
    @Expose
    private String applink;

    @SerializedName("webLink")
    @Expose
    private String webLink;

    public String getIconUrl() {
        return iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public String getApplink() {
        return applink;
    }

    public String getWebLink() {
        return webLink;
    }
}

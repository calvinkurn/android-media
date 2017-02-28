package com.tokopedia.core.gcm.data.source.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 2/23/17.
 */

public class MessagePushNotificationResponse {
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("full_name")
    private String fullname;
    @SerializedName("applinks")
    private String applink;
    @SerializedName("desc")
    private String description;

    public MessagePushNotificationResponse() {

    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getFullname() {
        return fullname;
    }

    public String getApplink() {
        return applink;
    }

    public String getDescription() {
        return description;
    }
}

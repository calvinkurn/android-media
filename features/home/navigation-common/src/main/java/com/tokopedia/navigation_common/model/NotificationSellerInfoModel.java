package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/26/18.
 */
public class NotificationSellerInfoModel {
    @SerializedName("notification")
    @Expose
    private Integer notification = 0;

    public Integer getNotification() {
        return notification;
    }

    public void setNotification(Integer notification) {
        this.notification = notification;
    }
}

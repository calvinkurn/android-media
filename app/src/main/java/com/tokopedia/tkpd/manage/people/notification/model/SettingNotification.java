
package com.tokopedia.tkpd.manage.people.notification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingNotification {

    @SerializedName("notification")
    @Expose
    private Notification notification;

    /**
     * 
     * @return
     *     The notification
     */
    public Notification getNotification() {
        return notification;
    }

    /**
     * 
     * @param notification
     *     The notification
     */
    public void setNotification(Notification notification) {
        this.notification = notification;
    }

}

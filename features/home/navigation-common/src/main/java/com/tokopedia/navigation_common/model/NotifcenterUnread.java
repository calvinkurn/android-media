
package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotifcenterUnread {

    @SerializedName("notif_unread")
    @Expose
    private String notifUnread;

    public String getNotifUnread() {
        return notifUnread;
    }

    public void setNotifUnread(String notifUnread) {
        this.notifUnread = notifUnread;
    }

}

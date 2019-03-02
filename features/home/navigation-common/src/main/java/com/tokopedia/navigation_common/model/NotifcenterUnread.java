
package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotifcenterUnread {

    public static final String NOTIF_99 = "99+";
    public static final int NOTIF_99_NUMBER = 99;
    public static final int NOTIF_100_NUMBER = 100;

    @SerializedName("notif_unread")
    @Expose
    private String notifUnread = "";

    public String getNotifUnread() {
        return notifUnread;
    }

    public void setNotifUnread(String notifUnread) {
        this.notifUnread = notifUnread;
    }

}

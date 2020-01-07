package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 11/17/17.
 */

public class InfoPenjualNotification {

    @Expose
    @SerializedName("notif_total_unread")
    private String notifUnreadString = "";

    @Expose
    @SerializedName("notif_total_unread_int")
    private Long notifUnreadInt = 0L;

    public String getNotifUnreadString() {
        return notifUnreadString;
    }

    public Long getNotifUnreadInt() {
        return notifUnreadInt;
    }

    public class Response {

        @SerializedName("notifcenter_total_unread")
        private InfoPenjualNotification infoPenjualNotification = new InfoPenjualNotification();

        public InfoPenjualNotification getInfoPenjualNotification() {
            return infoPenjualNotification;
        }
    }
}

package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 11/17/17.
 */

public class TopchatNotificationPojo {

    @SerializedName("notif_unreads")
    @Expose
    private int notifUnreads;

    public int getNotifUnreads() {
        return notifUnreads;
    }
}

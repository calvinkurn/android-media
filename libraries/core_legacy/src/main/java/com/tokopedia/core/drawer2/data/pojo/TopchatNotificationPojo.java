package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 11/17/17.
 */

public class TopchatNotificationPojo {

    @SerializedName("unreads")
    @Expose
    private int notifUnreads;

    @SerializedName("unreadsSeller")
    @Expose
    private int notifUnreadsSeller;

    @SerializedName("unreadsUser")
    @Expose
    private int notifUnreadsBuyer;

    public int getNotifUnreads() {
        return notifUnreads;
    }

    public int getNotifUnreadsSeller() {
        return notifUnreadsSeller;
    }

    public int getNotifUnreadsBuyer() {
        return notifUnreadsBuyer;
    }

}

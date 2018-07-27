package com.tokopedia.navigation.data.entity;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.navigation.data.NotificationsModel;

/**
 * Created by meta on 26/07/18.
 */
public class DrawerNotificationEntity {

    private NotificationsModel notifications;

    @SerializedName("total_notif")
    private String totalNotif;

    public NotificationsModel getNotifications() {
        return notifications;
    }

    public String getTotalNotif() {
        return totalNotif;
    }
}

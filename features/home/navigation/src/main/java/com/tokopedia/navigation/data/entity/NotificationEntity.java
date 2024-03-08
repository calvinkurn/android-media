package com.tokopedia.navigation.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.navigation_common.model.NotifcenterUnread;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.navigation_common.model.UserShopInfoModel;

/**
 * Created by meta on 26/07/18.
 */
public class NotificationEntity {

    @SerializedName("userShopInfo")
    @Expose
    private UserShopInfoModel shopInfo = new UserShopInfoModel();

    @SerializedName("notifications")
    @Expose
    private NotificationsModel notifications = new NotificationsModel();

    @SerializedName("notifcenter_unread")
    @Expose
    private NotifcenterUnread notifcenterUnread = new NotifcenterUnread();

    public NotificationsModel getNotifications() {
        return notifications;
    }

    public UserShopInfoModel getShopInfo() {
        return shopInfo;
    }

    public NotifcenterUnread getNotifcenterUnread() {
        return notifcenterUnread;
    }

}

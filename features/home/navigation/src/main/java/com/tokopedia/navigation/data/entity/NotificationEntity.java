package com.tokopedia.navigation.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.navigation_common.NotificationsModel;
import com.tokopedia.navigation_common.UserShopInfoModel;

/**
 * Created by meta on 26/07/18.
 */
public class NotificationEntity {

    @SerializedName("userShopInfo")
    @Expose
    private UserShopInfoModel shopInfo;

    @SerializedName("notifications")
    @Expose
    private NotificationsModel notifications;

    public NotificationsModel getNotifications() {
        return notifications;
    }

    public UserShopInfoModel getShopInfo() {
        return shopInfo;
    }
}

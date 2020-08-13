package com.tokopedia.navigation.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.navigation_common.model.FeedModel;
import com.tokopedia.navigation_common.model.HomeFlagModel;
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

    @SerializedName("feed_last_feeds")
    @Expose
    private FeedModel feed = new FeedModel();

    @SerializedName("homeFlag")
    @Expose
    private HomeFlagModel homeFlag = new HomeFlagModel();

    public NotificationsModel getNotifications() {
        return notifications;
    }

    public UserShopInfoModel getShopInfo() {
        return shopInfo;
    }

    public NotifcenterUnread getNotifcenterUnread() {
        return notifcenterUnread;
    }

    public FeedModel getFeed() {
        return feed;
    }

    public void setFeed(FeedModel feed) {
        this.feed = feed;
    }

    public HomeFlagModel getHomeFlag() {
        return homeFlag;
    }
}

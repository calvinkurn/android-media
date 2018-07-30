package com.tokopedia.navigation.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by meta on 25/07/18.
 */
public class NotificationEntity {

    private Notification notifications;

    public Notification getNotifications() {
        return notifications;
    }

    public class Notification {

        @SerializedName("total_cart")
        private String totalCart;

        @SerializedName("total_notif")
        private String totalNotif;

        private InboxEntity inbox;

        private ChatEntity chat;

        public String getTotalCart() {
            return totalCart;
        }

        public String getTotalNotif() {
            return totalNotif;
        }

        public InboxEntity getInbox() {
            return inbox;
        }

        public ChatEntity getChat() {
            return chat;
        }
    }
}

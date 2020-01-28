package com.tokopedia.core.drawer2.data.viewmodel;

/**
 * @author by nisie on 11/17/17.
 */

public class TopChatNotificationModel {
    private final int notifUnreads;
    private int notifUnreadsSeller = 0;
    private int notifUnreadsBuyer = 0;

    public TopChatNotificationModel(int notifUnreads) {
        this.notifUnreads = notifUnreads;
    }

    public int getNotifUnreads() {
        return notifUnreads;
    }

    public int getNotifUnreadsSeller() {
        return notifUnreadsSeller;
    }

    public int getNotifUnreadsBuyer() {
        return notifUnreadsBuyer;
    }

    public void setNotifUnreadsSeller(int notifUnreadsSeller) {
        this.notifUnreadsSeller = notifUnreadsSeller;
    }

    public void setNotifUnreadsBuyer(int notifUnreadsBuyer) {
        this.notifUnreadsBuyer = notifUnreadsBuyer;
    }
}

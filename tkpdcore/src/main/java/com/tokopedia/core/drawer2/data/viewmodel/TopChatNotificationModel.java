package com.tokopedia.core.drawer2.data.viewmodel;

/**
 * @author by nisie on 11/17/17.
 */

public class TopChatNotificationModel {
    private final int notifUnreads;

    public TopChatNotificationModel(int notifUnreads) {
        this.notifUnreads = notifUnreads;
    }

    public int getNotifUnreads() {
        return notifUnreads;
    }
}

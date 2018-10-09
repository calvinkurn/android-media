package com.tokopedia.core.gcm.notification.applink;

import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;

import java.util.List;

/**
 * Created by alvarisi on 2/24/17.
 */

public class DiscussionPushNotificationWrapper implements ApplinkVisitor<DiscussionPushNotificationWrapper> {
    private List<DiscussionPushNotification> discussionPushNotifications;

    public DiscussionPushNotificationWrapper() {
    }

    public List<DiscussionPushNotification> getDiscussionPushNotifications() {
        return discussionPushNotifications;
    }

    public void setDiscussionPushNotifications(List<DiscussionPushNotification> discussionPushNotifications) {
        this.discussionPushNotifications = discussionPushNotifications;
    }

    @Override
    public AbstractApplinkBuildAndShowNotification type(ApplinkTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}

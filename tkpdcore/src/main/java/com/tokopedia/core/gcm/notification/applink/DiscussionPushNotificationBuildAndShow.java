package com.tokopedia.core.gcm.notification.applink;

import android.content.Context;

import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;

import java.util.List;

/**
 * Created by alvarisi on 2/24/17.
 */

public class DiscussionPushNotificationBuildAndShow extends AbstractApplinkBuildAndShowNotification<DiscussionPushNotificationWrapper> {
    private List<DiscussionPushNotification> discussionPushNotifications;
    BuildAndShowNotification buildAndShowNotification;

    public DiscussionPushNotificationBuildAndShow(DiscussionPushNotificationWrapper discussionPushNotificationWrapper) {
        discussionPushNotifications = discussionPushNotificationWrapper.getDiscussionPushNotifications();
    }

    @Override
    public void process(Context context) {
        buildAndShowNotification = new BuildAndShowNotification(context);
    }
}

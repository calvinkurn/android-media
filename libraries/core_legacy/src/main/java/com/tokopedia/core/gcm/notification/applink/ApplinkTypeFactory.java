package com.tokopedia.core.gcm.notification.applink;

/**
 * Created by alvarisi on 2/23/17.
 */

public interface ApplinkTypeFactory {
    AbstractApplinkBuildAndShowNotification type(MessagePushNotificationWrapper messagePushNotificationWrapper);

    AbstractApplinkBuildAndShowNotification type(DiscussionPushNotificationWrapper elements);

    AbstractApplinkBuildAndShowNotification buildAndShowNotification();
}

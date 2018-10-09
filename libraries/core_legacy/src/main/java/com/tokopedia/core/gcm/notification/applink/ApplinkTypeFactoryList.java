package com.tokopedia.core.gcm.notification.applink;

/**
 * Created by alvarisi on 2/24/17.
 */

public class ApplinkTypeFactoryList implements ApplinkTypeFactory{
    public ApplinkTypeFactoryList() {
    }

    @Override
    public AbstractApplinkBuildAndShowNotification type(MessagePushNotificationWrapper messagePushNotificationWrapper) {
        return new MessagePushNotificationBuildAndShow(messagePushNotificationWrapper.getMessagePushNotifications());
    }

    @Override
    public AbstractApplinkBuildAndShowNotification type(DiscussionPushNotificationWrapper elements) {
        return new DiscussionPushNotificationBuildAndShow(elements);
    }

    @Override
    public AbstractApplinkBuildAndShowNotification buildAndShowNotification() {
        return null;
    }
}

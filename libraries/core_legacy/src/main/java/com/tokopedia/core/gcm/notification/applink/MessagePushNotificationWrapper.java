package com.tokopedia.core.gcm.notification.applink;

import com.tokopedia.core.gcm.domain.model.MessagePushNotification;

import java.util.List;

/**
 * Created by alvarisi on 2/24/17.
 */

public class MessagePushNotificationWrapper implements ApplinkVisitor<MessagePushNotificationWrapper>{
    private List<MessagePushNotification> messagePushNotifications;

    public MessagePushNotificationWrapper() {
    }

    public List<MessagePushNotification> getMessagePushNotifications() {
        return messagePushNotifications;
    }

    public void setMessagePushNotifications(List<MessagePushNotification> messagePushNotifications) {
        this.messagePushNotifications = messagePushNotifications;
    }

    @Override
    public AbstractApplinkBuildAndShowNotification type(ApplinkTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}

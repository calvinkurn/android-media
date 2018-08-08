package com.tokopedia.core.gcm.notification.applink;

import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;

import java.util.List;

/**
 * Created by alvarisi on 2/23/17.
 */

public interface ApplinkTypeFactory {
    AbstractApplinkBuildAndShowNotification type(MessagePushNotificationWrapper messagePushNotificationWrapper);

    AbstractApplinkBuildAndShowNotification type(DiscussionPushNotificationWrapper elements);

    AbstractApplinkBuildAndShowNotification buildAndShowNotification();
}

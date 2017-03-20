package com.tokopedia.tkpd.fcm;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;

/**
 * Created by Herdi_WORK on 12.01.17.
 */

public class CustomerMessagingService extends BaseNotificationMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        AppNotificationReceiver.Notifications.init(getApplication());
        AppNotificationReceiver.Notifications.onNotificationReceived(remoteMessage);
        AppNotificationReceiver.Notifications.onMoengageNotificationReceived(remoteMessage);
    }
}

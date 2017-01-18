package com.tokopedia.tkpd.fcm;

import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;

/**
 * Created by Herdi_WORK on 12.01.17.
 */

public class FCMMessagingService extends BaseNotificationMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);
        AppNotificationReceiver appNotificationReceiver = new AppNotificationReceiver();
        appNotificationReceiver.init(getApplication());
        appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
    }
}

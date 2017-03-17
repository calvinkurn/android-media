package com.tokopedia.tkpd.fcm;

import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;

/**
 * Created by Herdi_WORK on 12.01.17.
 */

public class CustomerMessagingService extends BaseNotificationMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);
        CommonUtils.dumper(data.toString());
//        AppNotificationReceiver.Notifications.init(getApplication());
//        AppNotificationReceiver.Notifications.onNotificationReceived(remoteMessage.getFrom(), data);
    }
}

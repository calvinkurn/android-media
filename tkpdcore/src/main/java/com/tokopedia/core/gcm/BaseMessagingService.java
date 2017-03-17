package com.tokopedia.core.gcm;

import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;

/**
 * Created by alvarisi on 3/17/17.
 */

public class BaseMessagingService extends BaseNotificationMessagingService {
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);
        CommonUtils.dumper(data.toString());
    }
}

package com.tokopedia.core.gcm.base;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by alvarisi on 3/17/17.
 */

public interface IAppNotificationReceiver {
    void init(Application application);

    void onNotificationReceived(String from, Bundle bundle);

    void onMoengageNotificationReceived(RemoteMessage message);
}

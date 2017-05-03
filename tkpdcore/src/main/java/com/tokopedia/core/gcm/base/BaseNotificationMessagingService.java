package com.tokopedia.core.gcm.base;

import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.core.gcm.utils.GCMUtils;

/**
 * @author by alvarisi on 1/10/17.
 */

public abstract class BaseNotificationMessagingService extends FirebaseMessagingService {

    public BaseNotificationMessagingService() {
    }

    protected Bundle convertMap(RemoteMessage message){
        return GCMUtils.convertMap(message.getData());
    }


}
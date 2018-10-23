package com.tokopedia.core.gcm;

import android.os.Bundle;

import com.tokopedia.core.gcm.model.NotificationPass;

/**
 * @author by alvarisi on 1/12/17.
 */

public interface Visitable {
    void proccessReceivedNotification(Bundle incomingMessage);

    NotificationPass getNotificationPassData();
}
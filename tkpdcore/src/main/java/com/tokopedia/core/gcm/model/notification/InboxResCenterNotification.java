package com.tokopedia.core.gcm.model.notification;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by alvarisi on 1/13/17.
 */

public class InboxResCenterNotification extends BaseNotification {
    protected InboxResCenterNotification(Context context) {
        super(context);
    }

    @Override
    protected void configureNotificationData(Bundle incomingMessage) {

    }
}

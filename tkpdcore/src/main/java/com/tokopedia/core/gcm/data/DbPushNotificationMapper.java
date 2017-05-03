package com.tokopedia.core.gcm.data;

import com.tokopedia.core.gcm.database.model.DbPushNotification;
import com.tokopedia.core.gcm.domain.PushNotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 2/22/17.
 */

public class DbPushNotificationMapper {
    public DbPushNotificationMapper() {
    }

    public List<PushNotification> transform(List<DbPushNotification> dbPushNotifications) {
        List<PushNotification> pushNotifications = new ArrayList<>();
        PushNotification pushNotification;
        for (DbPushNotification dbPushNotification : dbPushNotifications) {
            pushNotification = transform(dbPushNotification);
            if (pushNotification != null)
                pushNotifications.add(pushNotification);
        }
        return pushNotifications;
    }

    public PushNotification transform(DbPushNotification dbPushNotification) {
        PushNotification pushNotification = null;
        if (dbPushNotification != null) {
            pushNotification = new PushNotification();
            pushNotification.setCategory(dbPushNotification.getCategory());
            pushNotification.setResponse(dbPushNotification.getResponse());
        }
        return pushNotification;
    }
}

package com.tokopedia.pushnotif.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @author ricoharisin .
 */

@Database(name = PushNotificationDatabase.NAME, version = PushNotificationDatabase.VERSION)
public class PushNotificationDatabase {

    static final String NAME = "PushNotificationDB";

    static final int VERSION = 1;
}

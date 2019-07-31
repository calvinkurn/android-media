
package com.tokopedia.core.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.tokopedia.core.gcm.database.PushNotificationDao;
import com.tokopedia.core.gcm.database.model.DbPushNotification;

/**
 * Created by Nathan on 9/30/2016.
 */
@Database(entities = {DbPushNotification.class}, version = CoreLegacyDbFlowDatabase.VERSION)
public abstract class CoreLegacyDbFlowDatabase extends RoomDatabase {

    public abstract PushNotificationDao pushNotificationDao();

    public static final String NAME = "core";
    public static final int VERSION = 1;

    private static CoreLegacyDbFlowDatabase instance;

    public static CoreLegacyDbFlowDatabase getInstance(Context context)  {
        if  (instance == null) {
            synchronized (CoreLegacyDbFlowDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context);
                }
            }
        }

        return instance;
    }

    private static CoreLegacyDbFlowDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,
                CoreLegacyDbFlowDatabase.class,
                NAME).build();
    }
}

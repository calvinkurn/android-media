package com.tokopedia.notifications.database;

import android.content.Context;

import com.tokopedia.notifications.data.converters.CarouselConverter;
import com.tokopedia.notifications.data.converters.GridConverter;
import com.tokopedia.notifications.data.converters.JsonObjectConverter;
import com.tokopedia.notifications.data.converters.NotificationModeConverter;
import com.tokopedia.notifications.data.converters.NotificationStatusConverter;
import com.tokopedia.notifications.data.converters.ProductInfoConverter;
import com.tokopedia.notifications.data.converters.PushActionButtonConverter;
import com.tokopedia.notifications.database.pushRuleEngine.BaseNotificationDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.ButtonListConverter;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.ElapsedTimeDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.InAppDataDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.model.BaseNotificationModel;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {
        CMInApp.class,
        ElapsedTime.class,
        BaseNotificationModel.class
}, version = 1)

@TypeConverters({ButtonListConverter.class,
        NotificationModeConverter.class,
        NotificationStatusConverter.class,
        JsonObjectConverter.class,
        PushActionButtonConverter.class,
        CarouselConverter.class,
        GridConverter.class,
        ProductInfoConverter.class
})
public abstract class RoomNotificationDB extends RoomDatabase {

    public abstract InAppDataDao inAppDataDao();

    public abstract ElapsedTimeDao elapsedTimeDao();

    public abstract BaseNotificationDao baseNotificationDao();

    private static volatile RoomNotificationDB INSTANCE;

    public static RoomNotificationDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomNotificationDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomNotificationDB.class, "cm_push_notification")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

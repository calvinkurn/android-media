package com.tokopedia.notifications.database;

import android.content.Context;

import com.tokopedia.notifications.data.converters.CarouselConverter;
import com.tokopedia.notifications.data.converters.GridConverter;
import com.tokopedia.notifications.data.converters.PayloadExtraConverter;
import com.tokopedia.notifications.data.converters.JsonObjectConverter;
import com.tokopedia.notifications.data.converters.NotificationModeConverter;
import com.tokopedia.notifications.data.converters.NotificationStatusConverter;
import com.tokopedia.notifications.data.converters.ProductInfoConverter;
import com.tokopedia.notifications.data.converters.PushActionButtonConverter;
import com.tokopedia.notifications.data.converters.PushPersistentButtonConvertor;
import com.tokopedia.notifications.data.converters.PushPayloadExtraConverter;
import com.tokopedia.notifications.database.pushRuleEngine.BaseNotificationDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.ButtonListConverter;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.ElapsedTimeDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.InAppDataDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;
import com.tokopedia.notifications.model.BaseNotificationModel;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {
        CMInApp.class,
        ElapsedTime.class,
        BaseNotificationModel.class
}, version = 12)

@TypeConverters({ButtonListConverter.class,
        NotificationModeConverter.class,
        NotificationStatusConverter.class,
        JsonObjectConverter.class,
        PushActionButtonConverter.class,
        CarouselConverter.class,
        GridConverter.class,
        ProductInfoConverter.class,
        PushPersistentButtonConvertor.class,
        PayloadExtraConverter.class,
        PushPayloadExtraConverter.class
})
public abstract class RoomNotificationDB extends RoomDatabase {

    public abstract InAppDataDao inAppDataDao();

    public abstract ElapsedTimeDao elapsedTimeDao();

    public abstract BaseNotificationDao baseNotificationDao();

    private static volatile RoomNotificationDB INSTANCE;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `notificationProductType` TEXT");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `is_amplification` INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `is_amplification` INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `customValues` TEXT");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `campaignCode` TEXT");
        }
    };

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `elementId` TEXT");
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `visualCollapsedElementId` TEXT");
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `visualExpandedElementId` TEXT");
        }
    };

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `persistButton` TEXT");
        }
    };

    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `shopId` TEXT");
        }
    };

    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `is_big_image` INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `payloadExtra` TEXT");
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `payloadExtra` TEXT");
        }
    };

    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `push_payload_extra` TEXT");
        }
    };

    private static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `groupId` INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `groupName` TEXT");
        }
    };

    public static RoomNotificationDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomNotificationDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomNotificationDB.class, "cm_push_notification")
                            .addMigrations(
                                    MIGRATION_1_2,
                                    MIGRATION_2_3,
                                    MIGRATION_3_4,
                                    MIGRATION_4_5,
                                    MIGRATION_5_6,
                                    MIGRATION_6_7,
                                    MIGRATION_7_8,
                                    MIGRATION_8_9,
                                    MIGRATION_9_10,
                                    MIGRATION_10_11,
                                    MIGRATION_11_12
                            )
                            .fallbackToDestructiveMigrationOnDowngrade()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

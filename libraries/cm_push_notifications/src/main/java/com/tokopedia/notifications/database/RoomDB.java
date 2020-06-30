package com.tokopedia.notifications.database;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;

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

@Database(entities = {CMInApp.class, ElapsedTime.class, BaseNotificationModel.class}, version = 7)
@TypeConverters({ButtonListConverter.class,
        NotificationModeConverter.class,
        NotificationStatusConverter.class,
        JsonObjectConverter.class,
        PushActionButtonConverter.class,
        CarouselConverter.class,
        GridConverter.class,
        ProductInfoConverter.class
})
public abstract class RoomDB extends RoomDatabase {

    public abstract InAppDataDao inAppDataDao();

    public abstract ElapsedTimeDao elapsedTimeDao();

    public abstract BaseNotificationDao baseNotificationDao();

    private static volatile RoomDB INSTANCE;

    /**
     * Below Migration added to rename fields of table(inapp_data and elapsed_time) as columnInfo is not added to these table
     * and data coping is not required as doesn't contain any data*
     **/
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE inapp_data");
            database.execSQL("CREATE TABLE `inapp_data`" +
                    " (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`freq` INTEGER NOT NULL, `notificationType` TEXT, `e` INTEGER NOT NULL, `inAnim` TEXT, `s` TEXT," +
                    "`d` INTEGER NOT NULL, `st` INTEGER NOT NULL, `et` INTEGER NOT NULL, `ct` INTEGER NOT NULL," +
                    "`buf_time` INTEGER NOT NULL, `shown` INTEGER NOT NULL, `last_shown` INTEGER NOT NULL," +
                    "`ui_img` TEXT, `ui_appLink` TEXT, `ui_btnOri` TEXT, `ui_inAppButtons` TEXT, `ui_bg_img` TEXT, " +
                    "`ui_bg_clr` TEXT, `ui_bg_sc` TEXT, `ui_bg_sw` INTEGER, `ui_bg_rd` REAL, `ui_ttl_txt` TEXT, " +
                    "`ui_ttl_clr` TEXT, `ui_ttl_sz` TEXT, `ui_msg_txt` TEXT, `ui_msg_clr` TEXT, " +
                    "`ui_msg_sz` TEXT)");
            database.execSQL("DROP TABLE elapsed_time");
            database.execSQL("CREATE TABLE `elapsed_time`" +
                    " (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `elt` INTEGER NOT NULL)");

        }
    };


    /**
     * Below Migration added to add fields in table(inapp_data)*
     **/
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `campaignId` TEXT");
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `campaignUserToken` TEXT");
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `parentId` TEXT");
        }
    };

    /**
     * Below Migration added to Create BaseNotificationModel Table*
     **/
    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `BaseNotificationModel` " +
                    "(`notificationId` INTEGER NOT NULL, `campaignId` INTEGER NOT NULL, " +
                    "`priority` INTEGER NOT NULL, `title` TEXT, `detailMessage` TEXT, `message` TEXT, " +
                    "`icon` TEXT, `soundFileName` TEXT, `tribeKey` TEXT, `appLink` TEXT, `actionBtn` TEXT NOT NULL, " +
                    "`customValues` TEXT, `type` TEXT, `channelName` TEXT, `videoPush` TEXT, `subText` TEXT," +
                    " `visualCollapsedImg` TEXT, `visualExpandedImg` TEXT, `carouselIndex` INTEGER NOT NULL, " +
                    "`isVibration` INTEGER NOT NULL, `isSound` INTEGER NOT NULL, `isUpdatingExisting` INTEGER NOT NULL," +
                    " `carousel` TEXT NOT NULL, `grid` TEXT NOT NULL, `productInfo` TEXT NOT NULL, `parentId` " +
                    "INTEGER NOT NULL, `campaignUserToken` TEXT, `notificationStatus` INTEGER NOT NULL," +
                    " `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `notificationMode` INTEGER NOT NULL," +
                    " `media_fallback_url` TEXT, `media_high_quality_url` TEXT, `media_medium_quality_url` TEXT," +
                    " `media_low_quality_url` TEXT, `media_display_url` TEXT, `media_id` TEXT, PRIMARY KEY(`campaignId`))");
        }
    };

    /**
     * Below Migration added to drop and recreate inapp_data and BaseNotificationModel Tables due to encryption implementation*
     **/
    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS `inapp_data`");
            database.execSQL("CREATE TABLE IF NOT EXISTS `inapp_data` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`campaignId` TEXT, `freq` INTEGER NOT NULL, `notificationType` TEXT, `campaignUserToken` TEXT, " +
                    "`parentId` TEXT, `e` INTEGER NOT NULL, `inAnim` TEXT, `s` TEXT, `d` INTEGER NOT NULL, " +
                    "`st` INTEGER NOT NULL, `et` INTEGER NOT NULL, `ct` INTEGER NOT NULL, `buf_time` INTEGER NOT NULL, " +
                    "`shown` INTEGER NOT NULL, `last_shown` INTEGER NOT NULL, `ui_img` TEXT, `ui_appLink` TEXT, " +
                    "`ui_btnOri` TEXT, `ui_inAppButtons` TEXT, `ui_bg_img` TEXT, `ui_bg_clr` TEXT, " +
                    "`ui_bg_sc` TEXT, `ui_bg_sw` INTEGER, `ui_bg_rd` REAL, `ui_ttl_txt` TEXT, " +
                    "`ui_ttl_clr` TEXT, `ui_ttl_sz` TEXT, `ui_msg_txt` TEXT, `ui_msg_clr` TEXT, `ui_msg_sz` TEXT)");
            database.execSQL("DROP TABLE IF EXISTS `BaseNotificationModel`");
            database.execSQL("CREATE TABLE IF NOT EXISTS `BaseNotificationModel` (`notificationId` INTEGER NOT NULL," +
                    " `campaignId` INTEGER NOT NULL, `priority` INTEGER NOT NULL, `title` TEXT, `detailMessage` TEXT," +
                    " `message` TEXT, `icon` TEXT, `soundFileName` TEXT, `tribeKey` TEXT, `appLink` TEXT, `actionBtn` " +
                    "TEXT NOT NULL, `customValues` TEXT, `type` TEXT, `channelName` TEXT, `videoPush` TEXT, `subText`" +
                    " TEXT, `visualCollapsedImg` TEXT, `visualExpandedImg` TEXT, `carouselIndex` INTEGER NOT NULL, " +
                    "`isVibration` INTEGER NOT NULL, `isSound` INTEGER NOT NULL, `isUpdatingExisting` INTEGER NOT NULL," +
                    " `carousel` TEXT NOT NULL, `grid` TEXT NOT NULL, `productInfo` TEXT NOT NULL, `parentId` INTEGER NOT NULL," +
                    " `campaignUserToken` TEXT, `notificationStatus` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` " +
                    "INTEGER NOT NULL, `notificationMode` INTEGER NOT NULL, `media_fallback_url` TEXT, " +
                    "`media_high_quality_url` TEXT, `media_medium_quality_url` TEXT, `media_low_quality_url` TEXT, " +
                    "`media_display_url` TEXT, `media_id` TEXT, PRIMARY KEY(`campaignId`))");
        }
    };

    /**
     * Below Migration added to drop and recreate inapp_data and BaseNotificationModel Tables due to encryption implementation*
     **/
    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `inapp_data` ADD COLUMN `is_test` INTEGER NOT NULL");
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `is_test` INTEGER NOT NULL");
        }
    };

    /**
     * Migration for adding notification attribution entities
     * @column: transId
     * @column: userTransId
     * @column: userId
     * @column: shopId
     * @column: notifcenterBlastId
     */
    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` " +
                    "ADD COLUMN `transId` TEXT, " +
                    "ADD COLUMN `userTransId` TEXT, " +
                    "ADD COLUMN `userId` TEXT, " +
                    "ADD COLUMN `shopId` TEXT, " +
                    "ADD COLUMN `notifcenterBlastId` TEXT");
        }
    };

    /**
     * Migration for adding notification center webHook entity
     * @column: webhook_params
     */
    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `BaseNotificationModel` ADD COLUMN `webhook_params` TEXT");
        }
    };

    public static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, "inapp_database")
                            .addMigrations(
                                    MIGRATION_1_2,
                                    MIGRATION_2_3,
                                    MIGRATION_3_4,
                                    MIGRATION_4_5,
                                    MIGRATION_5_6,
                                    MIGRATION_6_7,
                                    MIGRATION_7_8
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
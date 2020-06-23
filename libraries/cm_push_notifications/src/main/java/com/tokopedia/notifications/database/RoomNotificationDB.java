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

@Database(entities = {CMInApp.class, ElapsedTime.class, BaseNotificationModel.class}, version = 1)
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

    private static final Migration MIGRATION_0_1 = new Migration(0, 1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS `BaseNotificationModel`");
            database.execSQL("CREATE TABLE IF NOT EXISTS `BaseNotificationModel` (`notificationId` INTEGER" +
                    " NOT NULL, `campaignId` INTEGER NOT NULL, `priority` INTEGER NOT NULL, `title` TEXT," +
                    " `detailMessage` TEXT, `message` TEXT, `icon` TEXT, `soundFileName` TEXT, `tribeKey` " +
                    "TEXT, `appLink` TEXT, `actionBtn` TEXT NOT NULL, `customValues` TEXT, `type` TEXT, " +
                    "`channelName` TEXT, `videoPush` TEXT, `subText` TEXT, `visualCollapsedImg` TEXT, " +
                    "`visualExpandedImg` TEXT, `carouselIndex` INTEGER NOT NULL, `isVibration` INTEGER " +
                    "NOT NULL, `isSound` INTEGER NOT NULL, `isUpdatingExisting` INTEGER NOT NULL, " +
                    "`carousel` TEXT NOT NULL, `grid` TEXT NOT NULL, `productInfo` TEXT NOT NULL, " +
                    "`parentId` INTEGER NOT NULL, `campaignUserToken` TEXT, `notificationStatus`" +
                    " INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, " +
                    "`notificationMode` INTEGER NOT NULL, `is_test` INTEGER, `transId` TEXT," +
                    " `userTransId` TEXT, `userId` TEXT, `shopId` TEXT, `notifcenterBlastId` TEXT," +
                    " `webhook_params` TEXT, `media_fallback_url` TEXT, `media_high_quality_url`" +
                    " TEXT, `media_medium_quality_url` TEXT, `media_low_quality_url` TEXT, " +
                    "`media_display_url` TEXT, `media_id` TEXT, PRIMARY KEY(`campaignId`))");
            database.execSQL("DROP TABLE IF EXISTS `inapp_data`");
            database.execSQL("CREATE TABLE IF NOT EXISTS `inapp_data` (`id` INTEGER PRIMARY KEY " +
                    "AUTOINCREMENT NOT NULL, `campaignId` TEXT, `freq` INTEGER, `notificationType`" +
                    " TEXT, `campaignUserToken` TEXT, `parentId` TEXT, `e` INTEGER, `inAnim` TEXT, " +
                    "`s` TEXT, `d` INTEGER, `st` INTEGER, `et` INTEGER, `ct` " +
                    "INTEGER, `buf_time` INTEGER, `shown` INTEGER, `last_shown` " +
                    "INTEGER, `is_test` INTEGER, `perst_on` INTEGER, `is_interacted`" +
                    " INTEGER, `ui_img` TEXT, `ui_appLink` TEXT, `ui_btnOri` TEXT, `ui_inAppButtons` " +
                    "TEXT, `ui_bg_img` TEXT, `ui_bg_clr` TEXT, `ui_bg_sc` TEXT, `ui_bg_sw` INTEGER, `ui_bg_rd`" +
                    " REAL, `ui_ttl_txt` TEXT, `ui_ttl_clr` TEXT, `ui_ttl_sz` TEXT, `ui_msg_txt` TEXT," +
                    " `ui_msg_clr` TEXT, `ui_msg_sz` TEXT)");
        }
    };

    public static RoomNotificationDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomNotificationDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomNotificationDB.class, "cm_push_notification")
                            .fallbackToDestructiveMigration()
                            .addMigrations(MIGRATION_0_1)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

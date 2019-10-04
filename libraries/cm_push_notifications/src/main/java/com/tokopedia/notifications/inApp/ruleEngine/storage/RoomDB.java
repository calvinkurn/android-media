package com.tokopedia.notifications.inApp.ruleEngine.storage;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.ElapsedTimeDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.InAppDataDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

@Database(entities = {CMInApp.class, ElapsedTime.class}, version = 2)
@TypeConverters(ButtonListConverter.class)
public abstract class RoomDB extends RoomDatabase {

    public abstract InAppDataDao inAppDataDao();

    public abstract ElapsedTimeDao elapsedTimeDao();

    private static volatile RoomDB INSTANCE;

    /**
     * Below Migration added to rename fields of table(inapp_data and elapsed_time) as columnInfo is not added to these table
     * and data coping is not required as doesn't contain any data*
     **/
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
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

    public static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, "inapp_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
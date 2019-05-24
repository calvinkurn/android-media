package com.tokopedia.pushnotif.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tokopedia.pushnotif.db.dao.HistoryNotificationDao
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB


/**
 * @author okasurya on 5/22/19.
 */
@Database(entities = [HistoryNotificationDB::class], version = 2)
abstract class PushNotificationDB : RoomDatabase() {
    abstract fun historyNotificationDao(): HistoryNotificationDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: PushNotificationDB? = null

        static final Migration MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                // We increment the version as a flag of dbflow to room migration
                // Since we didn't alter the table, there's nothing else to do here.
            }
        };

        fun getInstance(context: Context): PushNotificationDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PushNotificationDB {
            return Room.databaseBuilder(
                    context,
                    PushNotificationDB::class.java,
                    PUSHNOTIF_DB)
                    .addMigrations(MIGRATION_1_2)
                    .build()
        }
    }
}

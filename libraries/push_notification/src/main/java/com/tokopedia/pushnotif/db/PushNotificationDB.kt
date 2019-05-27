package com.tokopedia.pushnotif.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
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

        private val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
                // we just do it as a flag that this db using room since version 2
            }
        }

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

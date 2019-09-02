package com.tokopedia.pushnotif.db

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
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
                    .build()
        }
    }
}

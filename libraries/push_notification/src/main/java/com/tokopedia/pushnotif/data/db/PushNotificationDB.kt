package com.tokopedia.pushnotif.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tokopedia.pushnotif.data.constant.PUSHNOTIF_DB
import com.tokopedia.pushnotif.data.constant.TRANSACTION_TABLE
import com.tokopedia.pushnotif.data.db.dao.HistoryNotificationDao
import com.tokopedia.pushnotif.data.db.dao.TransactionNotificationDao
import com.tokopedia.pushnotif.data.db.model.HistoryNotification
import com.tokopedia.pushnotif.data.db.model.TransactionNotification

/**
 * @author okasurya on 5/22/19.
 */
@Database(entities = [
    HistoryNotification::class,
    TransactionNotification::class
], version = 4)
abstract class PushNotificationDB : RoomDatabase() {

    abstract fun historyNotificationDao(): HistoryNotificationDao
    abstract fun transactionNotificationDao(): TransactionNotificationDao

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: PushNotificationDB? = null

        private val migration_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS $TRANSACTION_TABLE(
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `sender_name` TEXT,
                        `message` TEXT,
                        `notification_type` INTEGER,
                        `notification_id` INTEGER,
                        `transaction_id` TEXT,
                        `avatar_url` TEXT,
                        `applink` TEXT
                    )
                """.trimIndent())
            }
        }

        fun getInstance(context: Context): PushNotificationDB {
            return instance
                    ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PushNotificationDB {
            return Room.databaseBuilder(
                    context,
                    PushNotificationDB::class.java,
                    PUSHNOTIF_DB)
                    .addMigrations(migration_3_4)
                    .build()
        }
    }
}

package com.tokopedia.iris.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tokopedia.iris.util.DATABASE_NAME
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.dao.TrackingPerfDao
import com.tokopedia.iris.data.db.table.PerformanceTracking
import com.tokopedia.iris.data.db.table.Tracking

/**
 * @author okasurya on 10/18/18.
 */

@Database(entities = [Tracking::class, PerformanceTracking::class], version = 3)
abstract class IrisDb : RoomDatabase() {
    abstract fun trackingDao(): TrackingDao
    abstract fun trackingPerfDao(): TrackingPerfDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: IrisDb? = null

        fun getInstance(context: Context): IrisDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.execSQL("ALTER TABLE tracking ADD COLUMN appVersion TEXT not null default ''")
                } catch (e:Exception) {
                    // noop
                }
            }
        }

        @Suppress("SwallowedException")
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.execSQL("CREATE TABLE IF NOT EXISTS `tracking_perf` " +
                            "(`event` TEXT NOT NULL, `userId` TEXT NOT NULL, " +
                            "`deviceId` TEXT NOT NULL, `timeStamp` INTEGER NOT NULL, " +
                            "`appVersion` TEXT NOT NULL DEFAULT '', `carrier` TEXT NOT NULL, " +
                            "`lowPower` INTEGER NOT NULL, " +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
                } catch (e: Exception) {
                    // noop
                }
            }
        }

        private fun buildDatabase(context: Context): IrisDb {
            return Room.databaseBuilder(context, IrisDb::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3).build()
        }
    }
}
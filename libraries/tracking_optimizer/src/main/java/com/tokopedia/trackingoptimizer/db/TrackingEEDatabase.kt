package com.tokopedia.trackingoptimizer.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.tokopedia.trackingoptimizer.db.dao.TrackingEEDatabaseDao
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel

@Database(entities = [TrackingEEDbModel::class],
        version = TrackingDatabase.VERSION,
        exportSchema = false)
abstract class TrackingDatabase : RoomDatabase() {

    abstract fun getTrackingEEDao(): TrackingEEDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: TrackingDatabase? = null
        const val DB_NAME = "tokopedia_tracking_db"
        const val VERSION = 1

        @JvmStatic
        fun getInstance(context: Context): TrackingDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): TrackingDatabase {
            return Room.databaseBuilder(context.applicationContext, TrackingDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}

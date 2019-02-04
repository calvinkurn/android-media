package com.tokopedia.trackingoptimizer.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tokopedia.trackingoptimizer.db.dao.TrackingEEDatabaseDao
import com.tokopedia.trackingoptimizer.db.dao.TrackingEEFullDatabaseDao
import com.tokopedia.trackingoptimizer.db.dao.TrackingRegularDatabaseDao
import com.tokopedia.trackingoptimizer.db.dao.TrackingScreenNameDatabaseDao
import com.tokopedia.trackingoptimizer.db.model.TrackingEEDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingEEFullDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingRegularDbModel
import com.tokopedia.trackingoptimizer.db.model.TrackingScreenNameDbModel

@Database(entities = [TrackingEEDbModel::class, TrackingEEFullDbModel::class, TrackingRegularDbModel::class,
    TrackingScreenNameDbModel::class],
        version = TrackingDatabase.VERSION,
        exportSchema = false)
abstract class TrackingDatabase : RoomDatabase() {

    abstract fun getTrackingEEDao(): TrackingEEDatabaseDao
    abstract fun getTrackingEEFullDao(): TrackingEEFullDatabaseDao
    abstract fun getTrackingRegularDao(): TrackingRegularDatabaseDao
    abstract fun getTrackingScreenNameDao(): TrackingScreenNameDatabaseDao

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

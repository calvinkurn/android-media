package com.tokopedia.iris.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tokopedia.iris.DATABASE_NAME
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.table.Tracking

/**
 * @author okasurya on 10/18/18.
 */

@Database(entities = [Tracking::class], version = 1)
abstract class IrisDb : RoomDatabase() {
    abstract fun trackingDao(): TrackingDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: IrisDb? = null

        fun getInstance(context: Context): IrisDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): IrisDb {
            return Room.databaseBuilder(context, IrisDb::class.java, DATABASE_NAME).build()
        }
    }
}
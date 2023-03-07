package com.tokopedia.journeydebugger.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.journeydebugger.data.source.*

@Database(entities = [JourneyLogDB::class], version = 1)
abstract class JourneyDatabase : RoomDatabase() {

    abstract fun journeyLogDao(): JourneyLogDao

    companion object {

        private val DATABASE_NAME = "tkpd_journey_log"

        @Volatile
        private var instance: JourneyDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): JourneyDatabase {
            var r = instance
            if (r == null) {
                synchronized(lock) {
                    r = instance
                    if (r == null) {
                        r = Room.databaseBuilder(context,
                            JourneyDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration().build()
                        instance = r
                    }
                }
            }
            return r!!
        }
    }
}


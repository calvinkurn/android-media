package com.tokopedia.analyticsdebugger.cassava.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDao

@Database(entities = [GtmLogDB::class], version = 1)
abstract class CassavaDatabase: RoomDatabase() {

    abstract fun cassavaDao(): GtmLogDao

    companion object {

        private val DATABASE_NAME = "cassava_db"

        @Volatile
        private var instance: CassavaDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): CassavaDatabase {
            var r = instance
            if (r == null) {
                synchronized(lock) {
                    r = instance
                    if (r == null) {
                        r = Room.databaseBuilder(context,
                            CassavaDatabase::class.java, DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration().build()
                        instance = r
                    }
                }
            }
            return r!!
        }
    }
}
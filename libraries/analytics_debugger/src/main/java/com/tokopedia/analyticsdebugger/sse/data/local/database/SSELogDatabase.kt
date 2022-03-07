package com.tokopedia.analyticsdebugger.sse.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.analyticsdebugger.sse.data.local.dao.SSELogDao
import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
@Database(entities = [SSELogEntity::class], version = 1)
abstract class SSELogDatabase: RoomDatabase() {

    abstract fun sseLogDao(): SSELogDao

    companion object {

        private val DATABASE_NAME = "tkpd_sse_logging"

        @Volatile
        private var instance: SSELogDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): SSELogDatabase {
            var r = instance
            if (r == null) {
                synchronized(lock) {
                    r = instance
                    if (r == null) {
                        r = Room.databaseBuilder(context,
                            SSELogDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration().build()
                        instance = r
                    }
                }
            }
            return r!!
        }
    }
}
package com.tokopedia.analyticsdebugger.websocket.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.analyticsdebugger.websocket.data.local.dao.WebSocketLogDao
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.WebSocketLogEntity

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

@Database(entities = [WebSocketLogEntity::class], version = 1)
abstract class WebSocketLogDatabase: RoomDatabase() {

    abstract fun webSocketLogDao(): WebSocketLogDao

    companion object {
        private val DATABASE_NAME = "tkpd_websocket_logging"

        @Volatile
        private var instance: WebSocketLogDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): WebSocketLogDatabase {
            var r = instance
            if(r == null) {
                synchronized(lock) {
                    r = instance
                    if(r == null) {
                        r = Room.databaseBuilder(context,
                                WebSocketLogDatabase::class.java, DATABASE_NAME
                            ).fallbackToDestructiveMigration().build()
                        instance = r
                    }
                }
            }
            return r!!
        }
    }
}
package com.tokopedia.analyticsdebugger.websocket.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.analyticsdebugger.websocket.data.local.dao.PlayWebSocketLogDao
import com.tokopedia.analyticsdebugger.websocket.data.local.dao.TopchatWebSocketLogDao
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.TopchatWebSocketLogEntity

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

@Database(
    entities = [
        PlayWebSocketLogEntity::class,
        TopchatWebSocketLogEntity::class,
    ], version = 4
)
abstract class WebSocketLogDatabase : RoomDatabase() {

    abstract fun playWebSocketLogDao(): PlayWebSocketLogDao
    abstract fun topchatWebSocketLogDao(): TopchatWebSocketLogDao

    companion object {
        private const val DATABASE_NAME = "tkpd_websocket_logging"

        @Volatile private var instance: WebSocketLogDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): WebSocketLogDatabase {
            return instance ?: synchronized(lock) {
                Room.databaseBuilder(
                    context,
                    WebSocketLogDatabase::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                    .also {
                        instance = it
                    }
            }
        }
    }
}

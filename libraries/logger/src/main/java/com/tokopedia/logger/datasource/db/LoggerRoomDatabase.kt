package com.tokopedia.logger.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Logger::class], version = 2, exportSchema = false)
abstract class LoggerRoomDatabase: RoomDatabase() {
    abstract fun logDao(): LoggerDao

    companion object {
        @Volatile
        private var INSTANCE: LoggerRoomDatabase? = null

        fun getDatabase(context: Context): LoggerRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoggerRoomDatabase::class.java,
                    "log_database").fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun close(){
            if (INSTANCE?.isOpen!!){
                INSTANCE?.close()
            }
        }
    }
}
package com.tokopedia.logger.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Logger::class], version = 1, exportSchema = false)
abstract class LoggerRoomDatabase: RoomDatabase() {
    abstract fun logDao(): LoggerDao

    companion object {
        @Volatile
        private var INSTANCE: LoggerRoomDatabase? = null
        private var isCreated = false

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

        fun checkCreated(context: Context): Boolean {
            isCreated = context.getDatabasePath("log_database").exists()
            return isCreated
        }

        fun close(){
            if (INSTANCE?.isOpen!!){
                INSTANCE?.close()
            }
        }
    }
}
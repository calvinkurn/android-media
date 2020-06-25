package com.tkpd.remoteresourcerequest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ResourceEntry::class],
    version = 1
)
abstract class ResourceDB : RoomDatabase() {

    abstract val resourceEntryDao: ResourceEntryDao

    companion object {
        const val DATABASE_NAME = "db_resource"

        @Volatile
        private var INSTANCE: ResourceDB? = null

        internal fun getDatabase(context: Context): ResourceDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResourceDB::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

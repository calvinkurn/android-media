package com.tokopedia.cachemanager.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tokopedia.cachemanager.db.PersistentCacheDatabase.Companion.VERSION
import com.tokopedia.cachemanager.db.dao.PersistentCacheDatabaseDao
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel

@Database(entities = [PersistentCacheDbModel::class],
        version = VERSION,
        exportSchema = false)
abstract class PersistentCacheDatabase : RoomDatabase() {

    abstract fun getPersistentCacheDao(): PersistentCacheDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: PersistentCacheDatabase? = null
        const val CACHE_DB_NAME = "tokopedia_cache_db"
        const val VERSION = 1

        @JvmStatic
        fun getInstance(context: Context): PersistentCacheDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                        ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): PersistentCacheDatabase {
            return Room.databaseBuilder(context.applicationContext, PersistentCacheDatabase::class.java, CACHE_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }
}

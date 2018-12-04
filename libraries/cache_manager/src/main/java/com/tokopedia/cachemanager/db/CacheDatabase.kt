package com.tokopedia.cachemanager.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tokopedia.cachemanager.db.CacheDatabase.Companion.VERSION
import com.tokopedia.cachemanager.db.dao.PersistentCacheDatabaseDao
import com.tokopedia.cachemanager.db.dao.SaveInstanceCacheDatabaseDao
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel
import com.tokopedia.cachemanager.db.model.SaveInstanceCacheDbModel

@Database(entities = [PersistentCacheDbModel::class, SaveInstanceCacheDbModel::class],
        version = VERSION,
        exportSchema = false)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun getPersistentCacheDao(): PersistentCacheDatabaseDao
    abstract fun getSaveInstanceCacheDao(): SaveInstanceCacheDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: CacheDatabase? = null
        const val CACHE_DB_NAME = "tokopedia_cache_db"
        const val VERSION = 1

        @JvmStatic
        fun getInstance(context: Context): CacheDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                        ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): CacheDatabase {
            return Room.databaseBuilder(context.applicationContext, CacheDatabase::class.java, CACHE_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }
}
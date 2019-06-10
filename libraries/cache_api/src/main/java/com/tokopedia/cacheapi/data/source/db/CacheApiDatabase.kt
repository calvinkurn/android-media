package com.tokopedia.cacheapi.data.source.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tokopedia.cacheapi.data.source.db.dao.CacheApiDataDao
import com.tokopedia.cacheapi.data.source.db.dao.CacheApiVersionDao
import com.tokopedia.cacheapi.data.source.db.dao.CacheApiWhitelistDao
import com.tokopedia.cacheapi.data.source.db.model.CacheApiData
import com.tokopedia.cacheapi.data.source.db.model.CacheApiVersion
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist

@Database(entities = [CacheApiVersion::class, CacheApiWhitelist::class, CacheApiData::class],
        version = CacheApiDatabase.VERSION,
        exportSchema = false)
abstract class CacheApiDatabase : RoomDatabase() {

    abstract fun getCacheApiVersionDao(): CacheApiVersionDao
    abstract fun getCacheApiWhitelistDao(): CacheApiWhitelistDao
    abstract fun getCacheApiDataDao(): CacheApiDataDao



    companion object {
        @Volatile
        private var INSTANCE: CacheApiDatabase? = null
        const val CACHE_DB_NAME = "cache_api_db"
        const val VERSION = 1

        @JvmStatic
        fun getInstance(context: Context): CacheApiDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                        ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): CacheApiDatabase {
            return Room.databaseBuilder(context.applicationContext, CacheApiDatabase::class.java, CACHE_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }
}

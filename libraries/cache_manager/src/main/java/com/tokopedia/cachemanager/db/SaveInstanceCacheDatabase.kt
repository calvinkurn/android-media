
package com.tokopedia.cachemanager.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.tokopedia.cachemanager.db.SaveInstanceCacheDatabase.Companion.VERSION
import com.tokopedia.cachemanager.db.dao.SaveInstanceCacheDatabaseDao
import com.tokopedia.cachemanager.db.model.SaveInstanceCacheDbModel

@Database(entities = [SaveInstanceCacheDbModel::class],
        version = VERSION,
        exportSchema = false)
abstract class SaveInstanceCacheDatabase : RoomDatabase() {

    abstract fun getSaveInstanceCacheDao(): SaveInstanceCacheDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: SaveInstanceCacheDatabase? = null
        const val VERSION = 1

        @JvmStatic
        fun getInstance(context: Context): SaveInstanceCacheDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                        ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): SaveInstanceCacheDatabase {
            return Room
                    .inMemoryDatabaseBuilder(context.applicationContext, SaveInstanceCacheDatabase::class.java)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }
}

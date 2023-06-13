package com.tokopedia.fakeresponse.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.fakeresponse.db.dao.GqlDao
import com.tokopedia.fakeresponse.db.dao.RestDao
import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.db.entities.RestRecord

@Database(entities = [GqlRecord::class, RestRecord::class], version = AppDatabase.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gqlDao(): GqlDao
    abstract fun restDao(): RestDao


    companion object {
        const val DB_VERSION = 2
        const val DATABASE_NAME = "responseDb.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
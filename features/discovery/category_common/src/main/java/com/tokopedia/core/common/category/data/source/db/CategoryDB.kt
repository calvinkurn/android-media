package com.tokopedia.core.common.category.data.source.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [CategoryDataBase::class], version = DBMetaData.DB_VERSION, exportSchema = false)
abstract class CategoryDB : RoomDatabase(){
    abstract fun getCategoryDao(): CategoryDao

    companion object {
        @Volatile private var INSTANCE: CategoryDB? = null

        @JvmStatic
        fun getInstance(context: Context): CategoryDB{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also{ INSTANCE = it}
            }
        }

        private fun buildDatabase(context: Context): CategoryDB {
            return Room.databaseBuilder(context, CategoryDB::class.java, DBMetaData.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }
}
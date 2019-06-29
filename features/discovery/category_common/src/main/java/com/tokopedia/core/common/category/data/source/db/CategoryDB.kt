package com.tokopedia.core.common.category.data.source.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
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
                    .addMigrations(migration_15_16)
                    .allowMainThreadQueries()
                    .build()
        }

        private val migration_15_16 = object : Migration(15, DBMetaData.DB_VERSION) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }
    }
}
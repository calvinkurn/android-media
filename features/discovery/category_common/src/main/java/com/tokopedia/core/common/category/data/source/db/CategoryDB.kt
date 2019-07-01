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

        private val migration_15_16 = object : Migration(DBMetaData.DB_OLD_VERSION, DBMetaData.DB_VERSION) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE ${DBMetaData.DB_TABLE}2(" +
                        "id INTEGER PRIMARY KEY," +
                        "name TEXT NOT NULL," +
                        "identifier TEXT NOT NULL," +
                        "weight INTEGER NOT NULL," +
                        "parentId INTEGER," +
                        "hasChild INTEGER NOT NULL)")
                database.execSQL("INSERT INTO ${DBMetaData.DB_TABLE}2 SELECT * FROM ${DBMetaData.DB_TABLE}")
                database.execSQL("DROP TABLE ${DBMetaData.DB_TABLE}")
                database.execSQL("ALTER TABLE ${DBMetaData.DB_TABLE}2 RENAME TO ${DBMetaData.DB_TABLE}")
            }
        }
    }
}
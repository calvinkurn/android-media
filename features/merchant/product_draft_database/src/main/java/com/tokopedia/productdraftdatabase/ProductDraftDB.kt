package com.tokopedia.productdraftdatabase

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context

@Database(entities = [ProductDraft::class], version = DBMetaData.DB_VERSION, exportSchema = false)
abstract class ProductDraftDB : RoomDatabase(){
    abstract fun getProductDraftDao(): ProductDraftDao

    companion object {
        @Volatile private var INSTANCE: ProductDraftDB? = null

        @JvmStatic
        fun getInstance(context: Context): ProductDraftDB{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also{ INSTANCE = it}
            }
        }

        private fun buildDatabase(context: Context): ProductDraftDB {
            return Room.databaseBuilder(context, ProductDraftDB::class.java, DBMetaData.DB_NAME)
                    .addMigrations(migration_6_7)
                    .allowMainThreadQueries()
                    .build()
        }

        private val migration_6_7 = object : Migration(6, DBMetaData.DB_VERSION) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE ${DBMetaData.DB_TABLE}2(" +
                        "id INTEGER PRIMARY KEY," +
                        "data TEXT NOT NULL," +
                        "${ProductDraft.COLUMN_IS_UPLOADING} INTEGER NOT NULL," +
                        "shopId TEXT," +
                        "version INTEGER NOT NULL)")
                database.execSQL("INSERT INTO ${DBMetaData.DB_TABLE}2 SELECT * FROM ${DBMetaData.DB_TABLE}")
                database.execSQL("DROP TABLE ${DBMetaData.DB_TABLE}")
                database.execSQL("ALTER TABLE ${DBMetaData.DB_TABLE}2 RENAME TO ${DBMetaData.DB_TABLE}")
            }
        }
    }
}
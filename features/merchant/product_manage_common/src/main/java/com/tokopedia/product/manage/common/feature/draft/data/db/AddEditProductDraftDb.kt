package com.tokopedia.product.manage.common.feature.draft.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.feature.draft.data.db.entity.AddEditProductDraftEntity

@Database(entities = [AddEditProductDraftEntity::class], version = AddEditProductDraftConstant.DB_VERSION_9, exportSchema = false)
abstract class AddEditProductDraftDb : RoomDatabase(){

    abstract fun getDraftDao(): AddEditProductDraftDao

    companion object {
        @Volatile
        private var INSTANCE: AddEditProductDraftDb? = null

        @JvmStatic
        fun getInstance(context: Context): AddEditProductDraftDb {
            return INSTANCE ?: synchronized(this){ INSTANCE ?: buildDatabase(context).also { INSTANCE = it } }
        }

        private fun buildDatabase(context: Context): AddEditProductDraftDb {
            return Room.databaseBuilder(context, AddEditProductDraftDb::class.java, AddEditProductDraftConstant.DB_NAME)
                    .addMigrations(migrationFromSevenToEight)
                    .addMigrations(migrationFromEightToNine)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

        private val migrationFromSevenToEight = object : Migration(AddEditProductDraftConstant.DB_VERSION_7, AddEditProductDraftConstant.DB_VERSION_8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE ${AddEditProductDraftConstant.DB_TABLE}2(" +
                        "id INTEGER PRIMARY KEY NOT NULL," +
                        "data TEXT NOT NULL," +
                        "${AddEditProductDraftConstant.DB_COLUMN_IS_UPLOADING} INTEGER NOT NULL," +
                        "shopId TEXT," +
                        "version INTEGER NOT NULL)")
                database.execSQL("INSERT INTO ${AddEditProductDraftConstant.DB_TABLE}2 SELECT * FROM ${AddEditProductDraftConstant.DB_TABLE}")
                database.execSQL("DROP TABLE ${AddEditProductDraftConstant.DB_TABLE}")
                database.execSQL("ALTER TABLE ${AddEditProductDraftConstant.DB_TABLE}2 RENAME TO ${AddEditProductDraftConstant.DB_TABLE}")
            }
        }



        private val migrationFromEightToNine = object : Migration(AddEditProductDraftConstant.DB_VERSION_8, AddEditProductDraftConstant.DB_VERSION_9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE ${AddEditProductDraftConstant.DB_TABLE}2(" +
                        "id INTEGER PRIMARY KEY NOT NULL," +
                        "data TEXT NOT NULL," +
                        "${AddEditProductDraftConstant.DB_COLUMN_IS_UPLOADING} INTEGER NOT NULL," +
                        "shopId TEXT," +
                        "version INTEGER NOT NULL)")
                database.execSQL("INSERT INTO ${AddEditProductDraftConstant.DB_TABLE}2 SELECT * FROM ${AddEditProductDraftConstant.DB_TABLE}")
                database.execSQL("DROP TABLE ${AddEditProductDraftConstant.DB_TABLE}")
                database.execSQL("ALTER TABLE ${AddEditProductDraftConstant.DB_TABLE}2 RENAME TO ${AddEditProductDraftConstant.DB_TABLE}")
            }
        }
    }
}
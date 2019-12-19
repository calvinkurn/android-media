package com.tokopedia.common.network.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [RestDatabaseModel::class], version = DbRestMetadata.VERSION, exportSchema = false)
abstract class RestDatabase: RoomDatabase(){
    abstract fun getRestDatabaseDao(): RestDatabaseDao

    companion object {
        @Volatile private var INSTANCE: RestDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): RestDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also{ INSTANCE = it}
            }
        }

        private fun buildDatabase(context: Context): RestDatabase {
            return Room.databaseBuilder(context, RestDatabase::class.java, DbRestMetadata.NAME)
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}
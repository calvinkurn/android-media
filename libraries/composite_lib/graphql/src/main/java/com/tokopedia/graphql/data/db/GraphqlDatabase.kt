package com.tokopedia.graphql.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [GraphqlDatabaseModel::class], version = DbMetadata.VERSION, exportSchema = false)
abstract class GraphqlDatabase: RoomDatabase(){
    abstract fun getGraphqlDatabaseDao(): GraphqlDatabaseDao

    companion object {
        @Volatile private var INSTANCE: GraphqlDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): GraphqlDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also{ INSTANCE = it}
            }
        }

        private fun buildDatabase(context: Context): GraphqlDatabase {
            return Room.databaseBuilder(context, GraphqlDatabase::class.java, DbMetadata.NAME)
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}
package com.tokopedia.graphql.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [GraphqlDatabaseModel::class, GQLQueryHashModel::class], version = DbMetadata.VERSION, exportSchema = false)
abstract class GraphqlDatabase: RoomDatabase(){
    abstract fun getGraphqlDatabaseDao(): GraphqlDatabaseDao
    abstract fun getGQLQueryHashMapDao(): GQLQueryHashMapDao
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
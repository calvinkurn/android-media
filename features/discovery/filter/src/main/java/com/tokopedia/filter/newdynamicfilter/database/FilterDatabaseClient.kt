package com.tokopedia.filter.newdynamicfilter.database

import android.content.Context
import androidx.room.Room

class FilterDatabaseClient private constructor(context: Context) {

    val filterDatabase: FilterDatabase = Room.databaseBuilder(context.applicationContext,
            FilterDatabase::class.java, "Filter.db").fallbackToDestructiveMigration().build()

    companion object {
        private var mInstance: FilterDatabaseClient? = null

        @Synchronized
        fun getInstance(context: Context): FilterDatabaseClient {
            if (mInstance == null) {
                mInstance = FilterDatabaseClient(context)
            }
            return mInstance as FilterDatabaseClient
        }
    }
}

package com.tokopedia.filter.newdynamicfilter.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FilterDBModel::class], version = 2)
abstract class FilterDatabase : RoomDatabase() {
    abstract fun filterDao(): FilterDao
}

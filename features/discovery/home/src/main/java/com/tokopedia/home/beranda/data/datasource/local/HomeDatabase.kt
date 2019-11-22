package com.tokopedia.home.beranda.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tokopedia.home.beranda.data.datasource.local.converter.Converters
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.domain.model.HomeData

@Database(entities = [HomeData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HomeDatabase: RoomDatabase() {
    abstract fun homeDao(): HomeDao

    companion object{
        fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, HomeDatabase::class.java, "HomeCache.db").fallbackToDestructiveMigration().build()
    }
}
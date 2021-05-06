package com.tokopedia.home.beranda.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tokopedia.home.beranda.data.datasource.local.HomeDatabase.Companion.homeVersion
import com.tokopedia.home.beranda.data.datasource.local.converter.Converters
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeRoomData

@Database(entities = [HomeRoomData::class, AtfCacheEntity::class], version = homeVersion, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HomeDatabase: RoomDatabase() {
    abstract fun homeDao(): HomeDao
    companion object{
        const val homeDatabase = "HomeCache.db"
        const val homeVersion = 8
        fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, HomeDatabase::class.java, homeDatabase).fallbackToDestructiveMigration().build()
    }
}
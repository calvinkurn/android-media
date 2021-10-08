package com.tokopedia.broadcaster.statsnerd.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tokopedia.broadcaster.statsnerd.data.converter.AudioTypeConverter
import com.tokopedia.broadcaster.statsnerd.data.converter.BitrateModeConverter
import com.tokopedia.broadcaster.statsnerd.data.dao.ChuckerDao
import com.tokopedia.broadcaster.statsnerd.data.entity.ChuckerLog

@Database(entities = [
    ChuckerLog::class
], version = 1)
@TypeConverters(
    AudioTypeConverter::class,
    BitrateModeConverter::class
)
abstract class BroadcasterChuckerDb : RoomDatabase() {

    abstract fun chuckerDao(): ChuckerDao

    companion object {
        private const val DATABASE_NAME = "internal_broadcaster"
        @Volatile private var instance: BroadcasterChuckerDb? = null

        fun instance(context: Context): BroadcasterChuckerDb {
            return instance ?: synchronized(BroadcasterChuckerDb::class) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BroadcasterChuckerDb::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build().also {
                    instance = it
                }
            }
        }
    }

}
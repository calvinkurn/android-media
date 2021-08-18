package com.tokopedia.broadcaster.chucker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tokopedia.broadcaster.chucker.data.converter.AudioTypeConverter
import com.tokopedia.broadcaster.chucker.data.converter.BitrateModeConverter
import com.tokopedia.broadcaster.chucker.data.dao.ChuckerDao
import com.tokopedia.broadcaster.chucker.data.entity.ChuckerLog

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
        @Volatile private var instance: BroadcasterChuckerDb? = null
        private const val databaseName = "internal_broadcaster"

        fun instance(context: Context): BroadcasterChuckerDb {
            return instance ?: synchronized(BroadcasterChuckerDb::class) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BroadcasterChuckerDb::class.java,
                    databaseName
                ).build().also {
                    instance = it
                }
            }
        }
    }

}
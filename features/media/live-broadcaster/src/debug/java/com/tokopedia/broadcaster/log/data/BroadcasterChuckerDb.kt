package com.tokopedia.broadcaster.log.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tokopedia.broadcaster.log.data.converter.AudioTypeConverter
import com.tokopedia.broadcaster.log.data.converter.BitrateModeConverter
import com.tokopedia.broadcaster.log.data.dao.NetworkLogDao
import com.tokopedia.broadcaster.log.data.entity.NetworkLog

@Database(entities = [
    NetworkLog::class
], version = 1)
@TypeConverters(
    AudioTypeConverter::class,
    BitrateModeConverter::class
)
abstract class BroadcasterChuckerDb : RoomDatabase() {

    abstract fun chuckerDao(): NetworkLogDao

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
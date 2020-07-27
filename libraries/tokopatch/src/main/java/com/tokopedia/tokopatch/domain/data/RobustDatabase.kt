package com.tokopedia.tokopatch.domain.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Author errysuprayogi on 09,June,2020
 */

@Database(entities = [DataResponse.Result::class], version = 1, exportSchema = false)
abstract class RobustDatabase : RoomDatabase() {

    abstract fun dataDao(): DataDao

    companion object {
        @Volatile
        private var INSTANCE: RobustDatabase? = null
        fun getDatabase(context: Context): RobustDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RobustDatabase::class.java,
                    "tokofix"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
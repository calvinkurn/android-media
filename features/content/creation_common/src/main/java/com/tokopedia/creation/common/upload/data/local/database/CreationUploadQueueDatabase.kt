package com.tokopedia.creation.common.upload.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.creation.common.upload.data.local.dao.CreationUploadQueueDao
import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Database(
    entities = [
        CreationUploadQueueEntity::class,
    ],
    version = 1
)
abstract class CreationUploadQueueDatabase : RoomDatabase() {

    abstract fun creationUploadQueueDao(): CreationUploadQueueDao

    companion object {
        private const val DATABASE_NAME = "tkpd_content_creation_upload_queue"

        @Volatile private var instance: CreationUploadQueueDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): CreationUploadQueueDatabase {
            return instance ?: synchronized(lock) {
                Room.databaseBuilder(
                    context,
                    CreationUploadQueueDatabase::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                    .also {
                        instance = it
                    }
            }
        }
    }
}

package com.tokopedia.product.manage.common.feature.draft.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.feature.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.UploadStatusDao
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity.UploadStatusEntity

@Database(entities = [AddEditProductDraftEntity::class, UploadStatusEntity::class], version = AddEditProductDraftConstant.DB_VERSION_10, exportSchema = false)
abstract class AddEditProductDraftDb : RoomDatabase(){

    abstract fun getDraftDao(): AddEditProductDraftDao

    abstract fun uploadStatusDao(): UploadStatusDao

    companion object {
        @Volatile
        private var INSTANCE: AddEditProductDraftDb? = null

        @JvmStatic
        fun getInstance(context: Context): AddEditProductDraftDb {
            return INSTANCE ?: synchronized(this){ INSTANCE ?: buildDatabase(context).also { INSTANCE = it } }
        }

        private fun buildDatabase(context: Context): AddEditProductDraftDb {
            return Room.databaseBuilder(context, AddEditProductDraftDb::class.java, AddEditProductDraftConstant.DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}
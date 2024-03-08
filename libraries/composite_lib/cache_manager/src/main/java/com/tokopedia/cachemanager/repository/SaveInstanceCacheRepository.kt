package com.tokopedia.cachemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.cachemanager.datasource.SaveInstanceCacheDataSource
import com.tokopedia.cachemanager.db.CacheDeletion
import com.tokopedia.cachemanager.db.model.SaveInstanceCacheDbModel

class SaveInstanceCacheRepository(context: Context) :
    CacheRepository<SaveInstanceCacheDbModel>(context) {

    companion object {

        @SuppressLint("StaticFieldLeak")
        var instance: SaveInstanceCacheRepository? = null

        @JvmStatic
        fun create(context: Context): SaveInstanceCacheRepository {
            return instance ?: SaveInstanceCacheRepository(context).also {
                instance = it
            }
        }
    }

    override fun createCacheDataSource() =
        SaveInstanceCacheDataSource(context)

    override fun needDeleteExpired(): Boolean {
        return CacheDeletion.isSaveInstanceNeedDeletion()
                && !CacheDeletion.isSaveInstanceJobActive
    }

}

package com.tokopedia.cachemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.cachemanager.datasource.PersistentCacheDataSource
import com.tokopedia.cachemanager.db.CacheDeletion
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel

class PersistentCacheRepository(context: Context) :
    CacheRepository<PersistentCacheDbModel>(context) {

    companion object {

        @SuppressLint("StaticFieldLeak")
        var instance: PersistentCacheRepository? = null

        @JvmStatic
        fun create(context: Context): PersistentCacheRepository {
            return instance ?: PersistentCacheRepository(context).also {
                instance = it
            }
        }
    }

    override fun createCacheDataSource() = PersistentCacheDataSource(context)

    override fun needDeleteExpired(): Boolean {
        return CacheDeletion.isPersistentNeedDeletion()
                && !CacheDeletion.isPersistentJobActive
    }

}

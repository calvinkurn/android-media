package com.tokopedia.cachemanager.repository

import android.content.Context
import com.tokopedia.cachemanager.datasource.PersistentCacheDataSource
import com.tokopedia.cachemanager.db.CacheDeletion
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel

class PersistentCacheRepository(context: Context) :
    CacheRepository<PersistentCacheDbModel>(context) {

    companion object {
        fun create(context: Context) = PersistentCacheRepository(context = context)
    }

    override fun createCacheDataSource() = PersistentCacheDataSource(context)

    override fun needDeleteExpired(): Boolean {
        return CacheDeletion.isPersistentNeedDeletion()
                && !CacheDeletion.isPersistentJobActive
    }

}

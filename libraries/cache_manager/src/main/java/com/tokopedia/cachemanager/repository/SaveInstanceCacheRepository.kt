package com.tokopedia.cachemanager.repository

import android.content.Context
import com.tokopedia.cachemanager.datasource.SaveInstanceCacheDataSource
import com.tokopedia.cachemanager.db.CacheDeletion
import com.tokopedia.cachemanager.db.model.SaveInstanceCacheDbModel

class SaveInstanceCacheRepository(context: Context) :
        CacheRepository<SaveInstanceCacheDbModel>(context) {

    override fun createCacheDataSource() =
            SaveInstanceCacheDataSource(context)

    override fun needDeleteExpired(): Boolean {
        return CacheDeletion.isSaveInstanceNeedDeletion()
                && !CacheDeletion.isSaveInstanceJobActive
    }

}

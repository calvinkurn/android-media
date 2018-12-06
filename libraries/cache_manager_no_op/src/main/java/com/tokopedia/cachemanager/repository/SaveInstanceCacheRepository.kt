package com.tokopedia.cachemanager.repository

import android.content.Context
import com.tokopedia.cachemanager.datasource.SaveInstanceCacheDataSource

class SaveInstanceCacheRepository(context: Context) :
        CacheRepository(context) {

    override fun createCacheDataSource() =
            SaveInstanceCacheDataSource(context)


}


package com.tokopedia.cachemanager.repository

import android.content.Context
import com.tokopedia.cachemanager.datasource.PersistentCacheDataSource

class PersistentCacheRepository(context: Context) :
        CacheRepository(context) {

    override fun createCacheDataSource() =
            PersistentCacheDataSource(context)

}

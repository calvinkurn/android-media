package com.tokopedia.cachemanager.db.model

import android.arch.persistence.room.Entity
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel.Companion.PERSISTENT_CACHE_TABLE_NAME

@Entity(tableName = PERSISTENT_CACHE_TABLE_NAME)
class PersistentCacheDbModel: CacheDbModel() {

    companion object {
        const val PERSISTENT_CACHE_TABLE_NAME = "persistent_cache_tb"
    }
}

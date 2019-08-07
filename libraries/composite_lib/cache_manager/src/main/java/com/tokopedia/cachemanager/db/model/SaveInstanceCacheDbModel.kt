package com.tokopedia.cachemanager.db.model

import android.arch.persistence.room.Entity
import com.tokopedia.cachemanager.db.model.SaveInstanceCacheDbModel.Companion.SAVE_INSTANCE_CACHE_TABLE_NAME

@Entity(tableName = SAVE_INSTANCE_CACHE_TABLE_NAME)
class SaveInstanceCacheDbModel: CacheDbModel() {

    companion object {
        const val SAVE_INSTANCE_CACHE_TABLE_NAME = "save_instance_cache_tb"
    }
}

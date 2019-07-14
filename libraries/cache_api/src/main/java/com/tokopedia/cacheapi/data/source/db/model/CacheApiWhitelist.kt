package com.tokopedia.cacheapi.data.source.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

@Entity(tableName = CacheApiWhitelist.TABLE_NAME, primaryKeys = [CacheApiWhitelist.COLUMN_HOST, CacheApiWhitelist.COLUMN_PATH])
class CacheApiWhitelist(
        @ColumnInfo(name = COLUMN_ID)
        val id: Long,
        @ColumnInfo(name = COLUMN_HOST)
        var host: String,
        @ColumnInfo(name = COLUMN_PATH)
        var path: String,
        @ColumnInfo(name = COLUMN_EXPIRED_TIME)
        val expiredTime: Long,
        @ColumnInfo(name = COLUMN_DYNAMIC_LINK)
        val dynamicLink: Boolean

) {
    companion object {
        const val TABLE_NAME = "cache_api_whitelist"
        const val COLUMN_ID = "id"
        const val COLUMN_HOST = "host"
        const val COLUMN_PATH = "path"
        const val COLUMN_EXPIRED_TIME = "expired_time"
        const val COLUMN_DYNAMIC_LINK = "dynamic_link"
    }
}
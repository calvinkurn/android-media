package com.tokopedia.cacheapi.data.source.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = CacheApiVersion.TABLE_NAME)
class CacheApiVersion(
        @PrimaryKey
        @ColumnInfo(name = COLUMN_VERSION)
        val version: String
) {
    companion object {
        const val TABLE_NAME = "cache_api_version"
        const val COLUMN_VERSION = "version"
    }
}

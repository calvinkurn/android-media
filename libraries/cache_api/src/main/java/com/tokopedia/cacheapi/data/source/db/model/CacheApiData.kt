package com.tokopedia.cacheapi.data.source.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = CacheApiData.TABLE_NAME, primaryKeys = [CacheApiData.COLUMN_HOST, CacheApiData.COLUMN_PATH, CacheApiData.COLUMN_REQUEST_PARAM, CacheApiData.COLUMN_METHOD])
class CacheApiData(
        @ColumnInfo(name = COLUMN_HOST)
        val host: String,
        @ColumnInfo(name = COLUMN_PATH)
        val path: String,
        @ColumnInfo(name = COLUMN_REQUEST_PARAM)
        val requestParam: String,
        @ColumnInfo(name = COLUMN_METHOD)
        val method: String,
        @ColumnInfo(name = COLUMN_RESPONSE_BODY)
        val responseBody: String,
        @ColumnInfo(name = COLUMN_RESPONSE_TIME)
        val responseTime: Long,
        @ColumnInfo(name = COLUMN_EXPIRED_TIME)
        val expiredTime: Long,
        @ColumnInfo(name = COLUMN_WHITE_LIST_ID)
        val whiteListId: Long
) {
    companion object {
        const val TABLE_NAME = "cache_api_data"
        const val COLUMN_HOST = "host"
        const val COLUMN_PATH = "path"
        const val COLUMN_METHOD = "method"
        const val COLUMN_WHITE_LIST_ID = "white_list_id"
        const val COLUMN_REQUEST_PARAM = "request_param"
        const val COLUMN_RESPONSE_BODY = "response_body"
        const val COLUMN_RESPONSE_TIME = "response_time"
        const val COLUMN_EXPIRED_TIME = "expired_time"
    }
}

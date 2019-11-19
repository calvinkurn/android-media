package com.tokopedia.cachemanager.db.model

import androidx.room.PrimaryKey

abstract class CacheDbModel {
    @PrimaryKey
    var key = ""
    var value: String = ""
    var expiredTime: Long = 0

}

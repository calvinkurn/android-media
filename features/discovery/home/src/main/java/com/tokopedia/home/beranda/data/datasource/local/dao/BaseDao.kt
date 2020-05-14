package com.tokopedia.home.beranda.data.datasource.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.tokopedia.home.beranda.domain.model.HomeData
import rx.Observable

abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insert(item: T)
}
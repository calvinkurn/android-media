package com.tokopedia.trackingoptimizer.db.dao

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update
import com.tokopedia.trackingoptimizer.db.model.TrackingDbModel

interface TrackingDatabaseDao<T : TrackingDbModel> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingle(model: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultiple(models: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(model: T)

    fun deleteByKey(key: String)
    fun deleteTable()
    fun getTrackingModel(key: String): T?
    fun getTrackingModelList(): Array<T>?
}

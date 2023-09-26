package com.tokopedia.trackingoptimizer.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
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

    fun hasRowExist(): Boolean
}

package com.tokopedia.common.network.data.db

import android.arch.persistence.room.*


@Dao
interface RestDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingle(restDatabaseModel: RestDatabaseModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultiple(restDatabaseModelList: List<RestDatabaseModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(restDatabaseModel: RestDatabaseModel)

    @Delete
    fun delete(restDatabaseModel: RestDatabaseModel)

    @Query("DELETE FROM ${DbRestMetadata.NAME}")
    fun deleteTable()

    @Query("SELECT * FROM ${DbRestMetadata.NAME} WHERE key LIKE :key LIMIT 1")
    fun getRestModel(key: String): RestDatabaseModel?
}
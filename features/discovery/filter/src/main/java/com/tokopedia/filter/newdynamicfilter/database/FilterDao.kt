package com.tokopedia.filter.newdynamicfilter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filterDBModel: FilterDBModel)

    @Query("SELECT * FROM FilterDBModel WHERE filter_id LIKE :query")
    fun getFilterDataById(query: String): FilterDBModel
}

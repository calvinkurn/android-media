package com.rahullohra.fakeresponse.db.dao

import androidx.room.*
import com.rahullohra.fakeresponse.db.entities.RestResponse

@Dao
interface RestDao {

    @Query("Select * FROM RestResponse")
    fun getAll(): List<RestResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(restResponse: RestResponse): Long

    @Update
    fun update(restResponse: RestResponse)

    @Query("Update RestResponse SET enabled =:isEnabled where id = :restResponseId")
    fun toggle(restResponseId: Int, isEnabled: Boolean)

    @Query("Select * FROM RestResponse where url = :formattedUrl AND httpMethod = :method AND enabled = :isEnabled")
    fun getRestResponse(formattedUrl: String, method:String, isEnabled: Boolean = true): RestResponse


    @Query("DELETE from RestResponse")
    fun deleteAll()
}
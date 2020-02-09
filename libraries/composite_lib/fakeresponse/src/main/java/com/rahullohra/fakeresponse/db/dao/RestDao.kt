package com.rahullohra.fakeresponse.db.dao

import androidx.room.*
import com.rahullohra.fakeresponse.db.entities.RestRecord

@Dao
interface RestDao {

    @Query("Select * FROM RestRecord")
    fun getAll(): List<RestRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(restRecord: RestRecord): Long

    @Update
    fun update(restRecord: RestRecord)

    @Query("Update RestRecord SET enabled =:isEnabled where id = :restResponseId")
    fun toggle(restResponseId: Int, isEnabled: Boolean)

    @Query("Select * FROM RestRecord where url = :formattedUrl AND httpMethod = :method AND enabled = :isEnabled")
    fun getRestResponse(
            formattedUrl: String,
            method: String,
            isEnabled: Boolean = true
    ): RestRecord

    @Query("Select * FROM RestRecord where id = :id")
    fun getRestResponse(id: Int): RestRecord


    @Query("DELETE from RestRecord")
    fun deleteAll()
}
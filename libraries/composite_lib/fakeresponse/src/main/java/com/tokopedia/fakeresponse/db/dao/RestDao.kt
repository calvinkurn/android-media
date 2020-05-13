package com.tokopedia.fakeresponse.db.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.tokopedia.fakeresponse.db.entities.RestRecord

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

    @Query("Select * FROM RestRecord where id in (:id)")
    fun getRestResponse(id: List<Int>): List<RestRecord>

    @Query("Select id FROM RestRecord ORDER BY id DESC LIMIT 1")
    fun getLastId(): Int

    @Query("DELETE from RestRecord")
    fun deleteAll()

    @RawQuery
    fun searchRecords(query: SupportSQLiteQuery): List<RestRecord>
}
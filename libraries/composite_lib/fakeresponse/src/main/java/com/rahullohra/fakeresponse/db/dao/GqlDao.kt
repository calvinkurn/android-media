package com.rahullohra.fakeresponse.db.dao

import androidx.room.*
import com.rahullohra.fakeresponse.db.entities.GqlRecord


@Dao
interface GqlDao {

    @Query("Select * FROM GqlRecord")
    fun getAll(): List<GqlRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGql(gqlRecord: GqlRecord): Long

    @Update
    fun updateGql(gqlRecord: GqlRecord)

    @Query("Update GqlRecord SET enabled =:isEnabled where id = :gqlId")
    fun toggleGql(gqlId: Int, isEnabled: Boolean)

    @Query("Select * FROM GqlRecord where gqlOperationName = :gqlOperationName AND enabled = :isEnabled")
    fun getRecordFromGqlQuery(gqlOperationName: String, isEnabled: Boolean = true): GqlRecord

    @Query("Select * FROM GqlRecord where id= :id")
    fun getRecordFromGqlQuery(id: Int): GqlRecord

    @Query("DELETE from GqlRecord")
    fun deleteAll()
}
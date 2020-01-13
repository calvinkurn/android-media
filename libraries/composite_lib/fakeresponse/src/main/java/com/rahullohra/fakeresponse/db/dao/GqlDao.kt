package com.rahullohra.fakeresponse.db.dao

import androidx.room.*
import com.rahullohra.fakeresponse.db.entities.FakeGql


@Dao
interface GqlDao {

    @Query("Select * FROM FakeGql")
    fun getAll(): List<FakeGql>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGql(fakeGql: FakeGql): Long

    @Update
    fun updateGql(fakeGql: FakeGql)

    @Query("Update FakeGql SET enabled =:isEnabled where id = :gqlId")
    fun toggleGql(gqlId: Int, isEnabled: Boolean)

    @Query("Select * FROM FakeGql where gqlOperationName = :gqlOperationName AND enabled = :isEnabled")
    fun getRecordFromGqlQuery(gqlOperationName: String, isEnabled: Boolean = true): FakeGql

    @Query("DELETE from FakeGql")
    fun deleteAll()
}
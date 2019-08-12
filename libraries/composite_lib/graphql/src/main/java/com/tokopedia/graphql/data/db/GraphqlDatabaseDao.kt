package com.tokopedia.graphql.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update


@Dao
interface GraphqlDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingle(graphqlDatabaseModel: GraphqlDatabaseModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultiple(graphqlDatabaseModel: List<GraphqlDatabaseModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(graphqlDatabaseModel: GraphqlDatabaseModel)

    @Delete
    fun delete(graphqlDatabaseModel: GraphqlDatabaseModel)

    @Query("DELETE FROM ${DbMetadata.NAME}")
    fun deleteTable()

    @Query("SELECT * FROM ${DbMetadata.NAME} WHERE key LIKE :key LIMIT 1")
    fun getGraphqlModel(key: String): GraphqlDatabaseModel?
}
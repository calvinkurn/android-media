package com.tokopedia.graphql.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GQLQueryHashMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingle(graphqlGQLQueryHashModel: GQLQueryHashModel)

    @Query("SELECT * FROM query_hash_map WHERE `key` LIKE :key LIMIT 1")
    fun getGraphqlModel(key: String): GQLQueryHashModel?

    @Query("DELETE FROM query_hash_map WHERE `key` LIKE :key")
    fun deleteQueryHash(key: String)
}
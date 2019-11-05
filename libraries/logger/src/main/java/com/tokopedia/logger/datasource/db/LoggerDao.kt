package com.tokopedia.logger.datasource.db

import androidx.room.*

@Dao
interface LoggerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(logger: Logger)

    @Query("SELECT count(*) FROM log_table")
    suspend fun getCountAll(): Int

    @Query("SELECT * FROM log_table WHERE post_priority == 1 ORDER BY timestamp LIMIT :entries")
    suspend fun getHighPostPrio(entries: Int): List<Logger>

    @Query("SELECT * FROM log_table WHERE post_priority == 2 ORDER BY timestamp LIMIT :entries")
    suspend fun getLowPostPrio(entries: Int): List<Logger>

    @Query("DELETE FROM log_table WHERE timestamp == :ts")
    suspend fun deleteEntry(ts: Long)

    @Query("DELETE FROM LOG_TABLE WHERE timestamp <= :ts")
    suspend fun deleteExpiredData(ts: Long)

    @Query("DELETE FROM log_table WHERE timestamp <= :ts and post_priority == 1")
    suspend fun deleteExpiredHighPrio(ts: Long)

    @Query("DELETE FROM log_table WHERE timestamp <= :ts and post_priority == 2")
    suspend fun deleteExpiredLowPrio(ts: Long)

}
package com.tokopedia.logger.datasource.db

import androidx.room.*

@Dao
interface LoggerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(logger: Logger)

    @Query("SELECT count(*) FROM log_table")
    suspend fun getCountAll(): Int

    @Query("SELECT * FROM LOG_TABLE WHERE server_channel == :serverChannel ORDER BY post_priority ASC, timestamp ASC LIMIT :limit")
    suspend fun getServerChannel(serverChannel: String, limit: Int): List<Logger>

    @Query("SELECT * FROM LOG_TABLE WHERE post_priority == 1 ORDER BY timestamp ASC LIMIT :limit")
    suspend fun getHighPostPrio(limit: Int): List<Logger>

    @Query("SELECT * FROM LOG_TABLE WHERE post_priority == 2 ORDER BY timestamp ASC LIMIT :limit")
    suspend fun getLowPostPrio(limit: Int): List<Logger>

    @Query("DELETE FROM LOG_TABLE WHERE timestamp == :ts")
    suspend fun deleteEntry(ts: Long)

    @Delete
    suspend fun deleteEntries(logger: List<Logger>)

    @Query("DELETE FROM LOG_TABLE WHERE timestamp <= :ts and post_priority == 1")
    suspend fun deleteExpiredHighPrio(ts: Long)

    @Query("DELETE FROM LOG_TABLE WHERE timestamp <= :ts and post_priority == 2")
    suspend fun deleteExpiredLowPrio(ts: Long)

    @Query("DELETE FROM LOG_TABLE WHERE timestamp <= :ts and post_priority == 3")
    suspend fun deleteExpiredSFPrio(ts: Long)

}
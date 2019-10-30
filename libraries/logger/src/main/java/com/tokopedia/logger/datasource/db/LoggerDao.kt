package com.tokopedia.logger.datasource.db

import androidx.room.*

@Dao
interface LoggerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(logger: Logger)

    @Query("DELETE FROM log_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM log_table ORDER BY timestamp LIMIT :entries")
    suspend fun getFirst(entries: Int): List<Logger>

    @Query("DELETE FROM log_table WHERE timestamp <= :ts")
    suspend fun deleteBeforeTs(ts: Long)

    @Query("SELECT count(*) FROM log_table")
    suspend fun getCountAll(): Int

    @Query("SELECT * FROM log_table ORDER BY timestamp")
    suspend fun getAll(): List<Logger>

    @Query("SELECT * FROM log_table WHERE priority ==  1 ORDER BY timestamp LIMIT :entries")
    suspend fun getFirstHighPrio(entries: Int): List<Logger>

    @Query("SELECT count(*) FROM log_table WHERE priority ==  1")
    suspend fun getCountHighPrio(): Int

    @Query("SELECT count(*) FROM log_table WHERE priority == 0")
    suspend fun getCountLowPrio(): Int

    @Query("DELETE FROM log_table WHERE timestamp NOT IN(SELECT timestamp FROM log_table WHERE priority == 1 ORDER BY timestamp LIMIT 100)")
    suspend fun deleteFirst()

    @Query("SELECT * FROM log_table WHERE priority == 0 ORDER BY timestamp LIMIT :entries")
    suspend fun getFirstLowPrio(entries: Int): List<Logger>

    @Query("DELETE FROM log_table WHERE timestamp == :ts")
    suspend fun deleteEntry(ts: Long)

    @Query("DELETE FROM log_table WHERE timestamp <= :ts and priority == 1")
    suspend fun deleteHighPrioBeforeTs(ts: Long)

    @Query("DELETE FROM log_table WHERE timestamp <= :ts and priority == 0")
    suspend fun deleteLowPrioBeforeTs(ts: Long)

}
package com.tokopedia.logger.datasource.db

import androidx.room.*

@Dao
interface LoggerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(logger: Logger)

    @Query("DELETE FROM log_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM log_table ORDER BY TimeStamp LIMIT :entries")
    suspend fun getFirst(entries: Int): List<Logger>

    @Query("DELETE FROM log_table WHERE TimeStamp <= :ts")
    suspend fun deleteBeforeTs(ts: Long)

    @Query("SELECT count(*) FROM log_table")
    suspend fun getCountAll(): Int

    @Query("SELECT * FROM log_table ORDER BY TimeStamp")
    suspend fun getAll(): List<Logger>

    @Query("SELECT * FROM log_table WHERE Priority ==  1 ORDER BY TimeStamp LIMIT :entries")
    suspend fun getFirstHighPrio(entries: Int): List<Logger>

    @Query("SELECT count(*) FROM log_table WHERE Priority ==  1")
    suspend fun getCountHighPrio(): Int

    @Query("SELECT count(*) FROM log_table WHERE Priority == 0")
    suspend fun getCountLowPrio(): Int

    @Query("DELETE FROM log_table WHERE TimeStamp NOT IN(SELECT TimeStamp FROM log_table WHERE Priority == 1 ORDER BY TimeStamp LIMIT 100)")
    suspend fun deleteFirst()

    @Query("SELECT * FROM log_table WHERE Priority == 0 ORDER BY TimeStamp LIMIT :entries")
    suspend fun getFirstLowPrio(entries: Int): List<Logger>

    @Query("DELETE FROM log_table WHERE TimeStamp == :ts")
    suspend fun deleteEntry(ts: Long)

    @Query("DELETE FROM log_table WHERE TimeStamp <= :ts and Priority == 1")
    suspend fun deleteHighPrioBeforeTs(ts: Long)

    @Query("DELETE FROM log_table WHERE TimeStamp <= :ts and Priority == 0")
    suspend fun deleteLowPrioBeforeTs(ts: Long)

}
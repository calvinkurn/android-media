package com.tokopedia.flight.search.data.cache.db.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.tokopedia.flight.search.data.cache.db.FlightJourneyTable
import com.tokopedia.flight.search.data.cache.db.JourneyAndRoutes

/**
 * @author by furqan on 13/04/2020
 */
@Dao
interface FlightJourneyDao {

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable")
    suspend fun findAllJourneys(): List<JourneyAndRoutes>

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable WHERE isReturn = 1")
    suspend fun findAllReturnJourneys(): List<JourneyAndRoutes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FlightJourneyTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: List<FlightJourneyTable>)

    @Update
    suspend fun update(item: FlightJourneyTable)

    @Update
    suspend fun update(items: List<FlightJourneyTable>)

    @Transaction
    @RawQuery
    suspend fun findFilteredJourneys(supportSQLiteQuery: SupportSQLiteQuery): List<JourneyAndRoutes>

    @Transaction
    @RawQuery
    suspend fun getSearchCount(supportSQLiteQuery: SupportSQLiteQuery): Int

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable WHERE FlightJourneyTable.id = :onwardJourneyId")
    suspend fun findJourneyById(onwardJourneyId: String): JourneyAndRoutes

    @Query("DELETE FROM FlightJourneyTable")
    suspend fun deleteTable()

    @Query("DELETE FROM FlightJourneyTable WHERE isReturn = 1")
    suspend fun deleteFlightSearchReturnData()

}
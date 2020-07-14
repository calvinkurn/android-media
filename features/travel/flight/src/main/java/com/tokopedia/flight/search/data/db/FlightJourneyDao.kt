package com.tokopedia.flight.search.data.db

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

/**
 * Created by Rizky on 01/10/18.
 */
@Dao
interface FlightJourneyDao {

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable")
    fun findAllJourneys(): List<JourneyAndRoutes>

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable WHERE isReturn = 1")
    fun findAllReturnJourneys(): List<JourneyAndRoutes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: FlightJourneyTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<FlightJourneyTable>)

    @Update
    fun update(item: FlightJourneyTable)

    @Update
    fun update(items: List<FlightJourneyTable>)

    @Transaction
    @RawQuery
    fun findFilteredJourneys(supportSQLiteQuery: SupportSQLiteQuery): List<JourneyAndRoutes>

    @Transaction
    @RawQuery
    fun getSearchCount(supportSQLiteQuery: SupportSQLiteQuery): Int

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable WHERE FlightJourneyTable.id = :onwardJourneyId")
    fun findJourneyById(onwardJourneyId: String?): JourneyAndRoutes?

    @Query("DELETE FROM FlightJourneyTable")
    fun deleteTable()

    @Query("DELETE FROM FlightJourneyTable")
    suspend fun deleteTableCoroutine()

    @Query("DELETE FROM FlightJourneyTable WHERE isReturn = 1")
    fun deleteFlightSearchReturnData()
}
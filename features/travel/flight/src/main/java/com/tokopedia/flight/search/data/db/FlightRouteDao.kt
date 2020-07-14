package com.tokopedia.flight.search.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Rizky on 01/10/18.
 */
@Dao
interface FlightRouteDao {
    @Query("SELECT * FROM FlightRouteTable WHERE FlightRouteTable.journeyId = :journeyId")
    fun getRoutesByJourneyId(journeyId: String?): List<FlightRouteTable?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(routes: List<FlightRouteTable?>?)

    @Query("DELETE FROM FlightRouteTable")
    fun deleteTable()

    @Query("DELETE FROM FlightRouteTable")
    suspend fun deleteTableCoroutine()

    @Query("DELETE FROM FlightRouteTable WHERE journeyId = :journeyId")
    fun deleteByJourneyId(journeyId: String?)
}
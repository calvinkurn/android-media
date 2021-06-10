package com.tokopedia.flight.search.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.flight.search.data.cache.db.FlightRouteTable

/**
 * @author by furqan on 13/04/2020
 */
@Dao
interface FlightRouteDao {
    @Query("SELECT * FROM FlightRouteTable WHERE FlightRouteTable.journeyId = :journeyId")
    suspend fun getRoutesByJourneyId(journeyId: String?): List<FlightRouteTable?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routes: List<FlightRouteTable?>?)

    @Query("DELETE FROM FlightRouteTable")
    suspend fun deleteTable()

    @Query("DELETE FROM FlightRouteTable WHERE journeyId = :journeyId")
    suspend fun deleteByJourneyId(journeyId: String?)
}
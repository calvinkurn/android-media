package com.tokopedia.flight.search.data.cache.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.flight.search.data.cache.db.FlightComboTable

/**
 * @author by furqan on 14/04/2020
 */
@Dao
interface FlightComboDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flightComboTable: List<FlightComboTable?>?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flightComboTable: FlightComboTable?)

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.onwardJourneyId = :onwardJourneyId")
    suspend fun findCombosByOnwardJourneyId(onwardJourneyId: String?): List<FlightComboTable>

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.returnJourneyId = :returnJourneyId")
    suspend fun findCombosByReturnJourneyId(returnJourneyId: String?): List<FlightComboTable>

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.returnJourneyId = :returnJourneyId AND FlightComboTable.onwardJourneyId = :onwardJourneyId")
    suspend fun findCombosByOnwardAndReturnJourneyId(onwardJourneyId: String?, returnJourneyId: String?): List<FlightComboTable>

    @Query("SELECT * FROM FlightComboTable")
    suspend fun findAllCombos(): List<FlightComboTable?>?

    @Query("DELETE FROM FlightComboTable")
    suspend fun deleteTable()

}
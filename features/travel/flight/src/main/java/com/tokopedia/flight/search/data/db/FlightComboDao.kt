package com.tokopedia.flight.search.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Rizky on 01/10/18.
 */
@Dao
interface FlightComboDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(flightComboTable: List<FlightComboTable?>?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(flightComboTable: FlightComboTable?)

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.onwardJourneyId = :onwardJourneyId")
    fun findCombosByOnwardJourneyId(onwardJourneyId: String?): List<FlightComboTable>

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.returnJourneyId = :returnJourneyId")
    fun findCombosByReturnJourneyId(returnJourneyId: String?): List<FlightComboTable>

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.returnJourneyId = :returnJourneyId AND FlightComboTable.onwardJourneyId = :onwardJourneyId")
    fun findCombosByOnwardAndReturnJourneyId(onwardJourneyId: String?, returnJourneyId: String?): List<FlightComboTable>

    @Query("SELECT * FROM FlightComboTable")
    fun findAllCombos(): List<FlightComboTable?>?

    @Query("DELETE FROM FlightComboTable")
    fun deleteTable()

    @Query("DELETE FROM FlightComboTable")
    suspend fun deleteTableCoroutine()
}
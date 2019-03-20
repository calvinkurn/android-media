package com.tokopedia.flight.passenger.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Dao
interface FlightPassengerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(flightPassengerTables: List<FlightPassengerTable>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(flightPassengerTable: FlightPassengerTable): Int

    @Query("SELECT * FROM FlightPassengerTable WHERE id = :idPassenger")
    fun findPassengerByIdPassenger(idPassenger: String): List<FlightPassengerTable>

    @Query("UPDATE FlightPassengerTable SET is_selected = :isSelected WHERE id = :idPassenger")
    fun updateFlightPassenger(idPassenger: String, isSelected: Int): Int

    @Query("SELECT * FROM FlightPassengerTable")
    fun findAllFlightPassenger(): List<FlightPassengerTable>

    @Query("DELETE FROM FlightPassengerTable")
    fun deleteAll(): Int

    @Query("DELETE FROM FlightPassengerTable WHERE id = :idPassenger")
    fun deletePassengerById(idPassenger: String): Int
}

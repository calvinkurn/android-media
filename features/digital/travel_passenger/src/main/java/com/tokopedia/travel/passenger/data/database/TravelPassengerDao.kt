package com.tokopedia.travel.passenger.data.database

import androidx.room.*

/**
 * @author by furqan on 02/01/2020
 */
@Dao
interface TravelPassengerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(travelPassengerTables: List<TravelPassengerTable>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(travelPassengerTable: TravelPassengerTable): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(travelPassengerTable: TravelPassengerTable): Int

    @Query("SELECT * FROM TravelPassengerTable WHERE name = :name")
    fun findTravelPassengerByName(name: String): TravelPassengerTable

    @Query("SELECT * FROM TravelPassengerTable WHERE idPassenger = :idPassenger")
    fun findTravelPassengerByIdPassenger(idPassenger: String): TravelPassengerTable

    @Query("UPDATE TravelPassengerTable SET selected = :isSelected WHERE idPassenger = :idPassenger")
    fun updateTravelPassenger(isSelected: Int, idPassenger: String): Int

    @Query("SELECT * FROM TravelPassengerTable")
    fun findAllTravelPassenger(): List<TravelPassengerTable>

    @Query("DELETE FROM TravelPassengerTable")
    fun deleteAll(): Int

    @Query("DELETE FROM TravelPassengerTable WHERE idPassenger = :idPassenger")
    fun deletePassengerById(idPassenger: String): Int

}
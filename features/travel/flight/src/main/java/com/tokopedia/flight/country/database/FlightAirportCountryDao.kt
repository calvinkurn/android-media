package com.tokopedia.flight.country.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Dao
interface FlightAirportCountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(flightAirportCountryTables: List<FlightAirportCountryTable>): List<Long>

    @Query("SELECT * FROM FlightAirportCountryTable")
    fun findAllPhoneCodes(): List<FlightAirportCountryTable>

    @Query("SELECT * FROM FlightAirportCountryTable WHERE country_name LIKE :query")
    fun getPhoneCodeByKeyword(query: String): List<FlightAirportCountryTable>

    @Query("SELECT * FROM FlightAirportCountryTable WHERE country_id LIKE :query")
    fun getCountryIdByKeyword(query: String): FlightAirportCountryTable

    @Query("DELETE FROM FlightAirportCountryTable")
    fun deleteAll(): Int

}

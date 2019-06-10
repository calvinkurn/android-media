package com.tokopedia.flight.country.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Dao
interface CountryPhoneCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(flightAirportCountryTables: List<CountryPhoneCodeTable>): List<Long>

    @Query("SELECT * FROM CountryPhoneCodeTable")
    fun findAllPhoneCodes(): List<CountryPhoneCodeTable>

    @Query("SELECT * FROM CountryPhoneCodeTable WHERE country_name LIKE :query")
    fun getPhoneCodeByKeyword(query: String): List<CountryPhoneCodeTable>

    @Query("SELECT * FROM CountryPhoneCodeTable WHERE country_id LIKE :query")
    fun getCountryIdByKeyword(query: String): CountryPhoneCodeTable

    @Query("DELETE FROM CountryPhoneCodeTable")
    fun deleteAll(): Int

}

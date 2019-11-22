package com.tokopedia.common.travel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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

    @Query("SELECT * FROM CountryPhoneCodeTable WHERE country_id LIKE :query")
    fun getCountryById(query: String): List<CountryPhoneCodeTable>

    @Query("DELETE FROM CountryPhoneCodeTable")
    fun deleteAll(): Int

}

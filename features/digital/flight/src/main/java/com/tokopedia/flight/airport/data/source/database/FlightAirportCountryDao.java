package com.tokopedia.flight.airport.data.source.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Dao
public interface FlightAirportCountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<FlightAirportCountryTable> flightAirportCountryTables);

    @Query("SELECT * FROM FlightAirportCountryTable")
    List<FlightAirportCountryTable> findAllPhoneCodes();

    @Query("SELECT * FROM FlightAirportCountryTable WHERE country_name LIKE :query")
    List<FlightAirportCountryTable> getPhoneCodeByKeyword(String query);

    @Query("SELECT * FROM FlightAirportCountryTable WHERE country_id LIKE :query")
    FlightAirportCountryTable getCountryIdByKeyword(String query);

    @Query("DELETE FROM FlightAirportCountryTable")
    int deleteAll();

}

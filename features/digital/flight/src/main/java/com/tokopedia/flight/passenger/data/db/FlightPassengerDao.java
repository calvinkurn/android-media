package com.tokopedia.flight.passenger.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Dao
public interface FlightPassengerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<FlightPassengerTable> flightPassengerTables);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(FlightPassengerTable flightPassengerTable);

    @Query("SELECT * FROM FlightPassengerTable WHERE id = :idPassenger")
    List<FlightPassengerTable> findPassengerByIdPassenger(String idPassenger);

    @Query("UPDATE FlightPassengerTable SET is_selected = :isSelected WHERE id = :idPassenger")
    int updateFlightPassenger(String idPassenger, int isSelected);

    @Query("SELECT * FROM FlightPassengerTable")
    List<FlightPassengerTable> findAllFlightPassenger();

    @Query("DELETE FROM FlightPassengerTable")
    int deleteAll();

    @Query("DELETE FROM FlightPassengerTable WHERE id = :idPassenger")
    int deletePassengerById(String idPassenger);
}

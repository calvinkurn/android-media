package com.tokopedia.flight.search.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Rizky on 01/10/18.
 */
@Dao
public interface FlightRouteDao {

    @Query("SELECT * FROM FlightRouteTable WHERE FlightRouteTable.journeyId = :journeyId")
    List<FlightRouteTable> getRoutesByJourneyId(String journeyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FlightRouteTable> routes);

    @Query("DELETE FROM FlightRouteTable")
    void deleteTable();

    @Query("DELETE FROM FlightRouteTable WHERE journeyId = :journeyId")
    void deleteByJourneyId(String journeyId);

}
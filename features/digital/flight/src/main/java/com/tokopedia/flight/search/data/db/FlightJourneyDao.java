package com.tokopedia.flight.search.data.db;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Rizky on 01/10/18.
 */
@Dao
public interface FlightJourneyDao {

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable")
    List<JourneyAndRoutes> findAllJourneys();

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable WHERE isReturn = 1")
    List<JourneyAndRoutes> findAllReturnJourneys();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FlightJourneyTable item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FlightJourneyTable> item);

    @Update
    void update(FlightJourneyTable item);

    @Update
    void update(List<FlightJourneyTable> items);

    @Transaction
    @RawQuery
    List<JourneyAndRoutes> findFilteredJourneys(SupportSQLiteQuery supportSQLiteQuery);

    @Transaction
    @RawQuery
    Integer getSearchCount(SupportSQLiteQuery supportSQLiteQuery);

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable WHERE FlightJourneyTable.id = :onwardJourneyId")
    JourneyAndRoutes findJourneyById(String onwardJourneyId);

    @Query("DELETE FROM FlightJourneyTable")
    void deleteTable();

    @Query("DELETE FROM FlightJourneyTable WHERE isReturn = 1")
    void deleteFlightSearchReturnData();
}
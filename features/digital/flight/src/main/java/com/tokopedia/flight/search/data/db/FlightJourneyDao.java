package com.tokopedia.flight.search.data.db;

import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;

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
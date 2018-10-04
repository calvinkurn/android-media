package com.tokopedia.flight.searchV2.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Rizky on 01/10/18.
 */
@Dao
public interface FlightJourneyDao {

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable")
    List<JourneyAndRoutesJava> findAllJourneys();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FlightJourneyTable item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FlightJourneyTable> item);

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable " +
            "WHERE " +
            "FlightJourneyTable.isReturn = :isReturn & " +
            "FlightJourneyTable.id = :journeyId & " +
            "FlightJourneyTable.isBestPairing = :isBestPairing")
    List<JourneyAndRoutesJava> findFilteredJourneys(boolean isReturn, String journeyId, boolean isBestPairing);

}
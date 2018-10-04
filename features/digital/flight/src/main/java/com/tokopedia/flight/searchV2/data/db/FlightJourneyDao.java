package com.tokopedia.flight.searchV2.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Rizky on 01/10/18.
 */
@Dao
public interface FlightJourneyDao {

    @Transaction
    @Query("SELECT * FROM FlightJourneyTable")
    List<JourneyAndRoutesJava> getJourneys();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FlightJourneyTable item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FlightJourneyTable> item);

}

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
public interface FlightComboDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FlightComboTable> flightComboTable);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FlightComboTable flightComboTable);

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.onwardJourneyId = :onwardJourneyId")
    List<FlightComboTable> findCombosByOnwardJourneyId(String onwardJourneyId);

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.returnJourneyId = :returnJourneyId")
    List<FlightComboTable> findCombosByReturnJourneyId(String returnJourneyId);

    @Query("SELECT * FROM FlightComboTable")
    List<FlightComboTable> findAllCombos();

    @Query("DELETE FROM FlightComboTable")
    void deleteTable();
}
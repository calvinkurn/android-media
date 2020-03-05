package com.tokopedia.flight.search.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Rizky on 01/10/18.
 */
@Dao
public interface FlightComboDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<FlightComboTable> flightComboTable);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FlightComboTable flightComboTable);

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.onwardJourneyId = :onwardJourneyId")
    List<FlightComboTable> findCombosByOnwardJourneyId(String onwardJourneyId);

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.returnJourneyId = :returnJourneyId")
    List<FlightComboTable> findCombosByReturnJourneyId(String returnJourneyId);

    @Query("SELECT * FROM FlightComboTable WHERE FlightComboTable.returnJourneyId = :returnJourneyId AND FlightComboTable.onwardJourneyId = :onwardJourneyId")
    List<FlightComboTable> findCombosByOnwardAndReturnJourneyId(String onwardJourneyId, String returnJourneyId);

    @Query("SELECT * FROM FlightComboTable")
    List<FlightComboTable> findAllCombos();

    @Query("DELETE FROM FlightComboTable")
    void deleteTable();
}
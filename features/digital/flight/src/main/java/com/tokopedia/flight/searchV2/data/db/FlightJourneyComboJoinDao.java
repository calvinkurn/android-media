package com.tokopedia.flight.searchV2.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Rizky on 02/10/18.
 */
@Dao
public interface FlightJourneyComboJoinDao {

    @Query("SELECT * FROM FlightJourneyTable " +
            "INNER JOIN FlightJourneyComboJoinTable " +
            "ON FlightJourneyTable.id = FlightJourneyComboJoinTable.journeyId " +
            "WHERE FlightJourneyComboJoinTable.comboJourneyId = :comboJourneyId")
    List<FlightJourneyTable> getJournyesForCombo(String comboJourneyId);

}

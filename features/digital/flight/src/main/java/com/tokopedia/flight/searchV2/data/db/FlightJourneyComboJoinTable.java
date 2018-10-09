package com.tokopedia.flight.searchV2.data.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

/**
 * Created by Rizky on 02/10/18.
 */
@Entity(primaryKeys = { "journeyId", "comboJourneyId" },
        foreignKeys = {
                @ForeignKey(entity = FlightJourneyTable.class,
                        parentColumns = "id",
                        childColumns = "journeyId"),
                @ForeignKey(entity = FlightComboTable.class,
                        parentColumns = "journeyId",
                        childColumns = "comboJourneyId")
        })
public class FlightJourneyComboJoinTable {
    @NonNull
    private String journeyId;
    @NonNull
    private String comboJourneyId;

    public FlightJourneyComboJoinTable(String journeyId, String comboJourneyId) {
        this.journeyId = journeyId;
        this.comboJourneyId = comboJourneyId;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public String getComboJourneyId() {
        return comboJourneyId;
    }
}

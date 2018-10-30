package com.tokopedia.flight.searchV2.data.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Rizky on 01/10/18.
 */
@Entity
public class FlightComboTable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String onwardJourneyId;
    private String returnJourneyId;
    private String comboId;
    private String adultPrice;
    private String childPrice;
    private String infantPrice;
    private int adultPriceNumeric;
    private int childPriceNumeric;
    private int infantPriceNumeric;
    private boolean isBestPairing;

    public FlightComboTable(String onwardJourneyId, String returnJourneyId, String comboId,
                            String adultPrice, String childPrice, String infantPrice,
                            int adultPriceNumeric, int childPriceNumeric, int infantPriceNumeric,
                            boolean isBestPairing) {
        this.onwardJourneyId = onwardJourneyId;
        this.returnJourneyId = returnJourneyId;
        this.comboId = comboId;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
        this.infantPrice = infantPrice;
        this.adultPriceNumeric = adultPriceNumeric;
        this.childPriceNumeric = childPriceNumeric;
        this.infantPriceNumeric = infantPriceNumeric;
        this.isBestPairing = isBestPairing;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public String getOnwardJourneyId() {
        return onwardJourneyId;
    }

    public String getReturnJourneyId() {
        return returnJourneyId;
    }

    public String getComboId() {
        return comboId;
    }

    public String getAdultPrice() {
        return adultPrice;
    }

    public String getChildPrice() {
        return childPrice;
    }

    public String getInfantPrice() {
        return infantPrice;
    }

    public int getAdultPriceNumeric() {
        return adultPriceNumeric;
    }

    public int getChildPriceNumeric() {
        return childPriceNumeric;
    }

    public int getInfantPriceNumeric() {
        return infantPriceNumeric;
    }

    public boolean isBestPairing() {
        return isBestPairing;
    }
}

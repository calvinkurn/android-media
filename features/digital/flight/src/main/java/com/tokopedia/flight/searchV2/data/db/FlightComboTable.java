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
    private String onwardAdultPrice;
    private String onwardChildPrice;
    private String onwardInfantPrice;
    private int onwardAdultPriceNumeric;
    private int onwardChildPriceNumeric;
    private int onwardInfantPriceNumeric;
    private String returnJourneyId;
    private String returnAdultPrice;
    private String returnChildPrice;
    private String returnInfantPrice;
    private int returnAdultPriceNumeric;
    private int returnChildPriceNumeric;
    private int returnInfantPriceNumeric;
    private String comboId;
    private boolean isBestPairing;

    public FlightComboTable(String onwardJourneyId, String onwardAdultPrice, String onwardChildPrice,
                            String onwardInfantPrice, int onwardAdultPriceNumeric,
                            int onwardChildPriceNumeric, int onwardInfantPriceNumeric,
                            String returnJourneyId, String returnAdultPrice, String returnChildPrice,
                            String returnInfantPrice, int returnAdultPriceNumeric,
                            int returnChildPriceNumeric, int returnInfantPriceNumeric,
                            String comboId, boolean isBestPairing) {
        this.onwardJourneyId = onwardJourneyId;
        this.onwardAdultPrice = onwardAdultPrice;
        this.onwardChildPrice = onwardChildPrice;
        this.onwardInfantPrice = onwardInfantPrice;
        this.onwardAdultPriceNumeric = onwardAdultPriceNumeric;
        this.onwardChildPriceNumeric = onwardChildPriceNumeric;
        this.onwardInfantPriceNumeric = onwardInfantPriceNumeric;
        this.returnJourneyId = returnJourneyId;
        this.returnAdultPrice = returnAdultPrice;
        this.returnChildPrice = returnChildPrice;
        this.returnInfantPrice = returnInfantPrice;
        this.returnAdultPriceNumeric = returnAdultPriceNumeric;
        this.returnChildPriceNumeric = returnChildPriceNumeric;
        this.returnInfantPriceNumeric = returnInfantPriceNumeric;
        this.comboId = comboId;
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

    public String getOnwardAdultPrice() {
        return onwardAdultPrice;
    }

    public String getOnwardChildPrice() {
        return onwardChildPrice;
    }

    public String getOnwardInfantPrice() {
        return onwardInfantPrice;
    }

    public int getOnwardAdultPriceNumeric() {
        return onwardAdultPriceNumeric;
    }

    public int getOnwardChildPriceNumeric() {
        return onwardChildPriceNumeric;
    }

    public int getOnwardInfantPriceNumeric() {
        return onwardInfantPriceNumeric;
    }

    public String getReturnJourneyId() {
        return returnJourneyId;
    }

    public String getReturnAdultPrice() {
        return returnAdultPrice;
    }

    public String getReturnChildPrice() {
        return returnChildPrice;
    }

    public String getReturnInfantPrice() {
        return returnInfantPrice;
    }

    public int getReturnAdultPriceNumeric() {
        return returnAdultPriceNumeric;
    }

    public int getReturnChildPriceNumeric() {
        return returnChildPriceNumeric;
    }

    public int getReturnInfantPriceNumeric() {
        return returnInfantPriceNumeric;
    }

    public String getComboId() {
        return comboId;
    }

    public boolean isBestPairing() {
        return isBestPairing;
    }
}

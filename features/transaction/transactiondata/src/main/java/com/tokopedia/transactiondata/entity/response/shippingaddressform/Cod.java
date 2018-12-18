package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 13/12/18.
 */
public class Cod {

    @SerializedName("is_cod")
    @Expose
    private boolean isCod;
    @SerializedName("counter_cod")
    @Expose
    private int counterCod;

    public Cod() {
    }

    public Cod(boolean isCod, int counterCod) {
        this.isCod = isCod;
        this.counterCod = counterCod;
    }

    public boolean isCod() {
        return isCod;
    }

    public void setCod(boolean cod) {
        isCod = cod;
    }

    public int getCounterCod() {
        return counterCod;
    }

    public void setCounterCod(int counterCod) {
        this.counterCod = counterCod;
    }
}

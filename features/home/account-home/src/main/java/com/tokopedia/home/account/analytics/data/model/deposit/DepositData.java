package com.tokopedia.home.account.analytics.data.model.deposit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 5/5/17.
 */

public class DepositData {

    @SerializedName("deposit_total")
    @Expose
    private String depositTotal = "";

    public String getDepositTotal() {
        return depositTotal;
    }

    public void setDepositTotal(String depositTotal) {
        this.depositTotal = depositTotal;
    }
}

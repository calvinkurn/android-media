
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Saldo {

    @SerializedName("deposit_fmt")
    @Expose
    private String depositFmt = "";
    @SerializedName("deposit")
    @Expose
    private Integer deposit = 0;

    public String getDepositFmt() {
        return depositFmt;
    }

    public Integer getDeposit() {
        return deposit;
    }
}

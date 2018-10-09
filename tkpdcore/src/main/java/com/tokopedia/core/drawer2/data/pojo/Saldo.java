
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Saldo {

    @SerializedName("deposit_fmt")
    @Expose
    private String depositFmt;
    @SerializedName("deposit")
    @Expose
    private Integer deposit;

    public String getDepositFmt() {
        return depositFmt;
    }

    public Integer getDeposit() {
        return deposit;
    }
}

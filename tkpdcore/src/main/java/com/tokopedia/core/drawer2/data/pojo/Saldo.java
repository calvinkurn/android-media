
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Saldo {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("deposit_fmt")
    @Expose
    private String depositFmt;
    @SerializedName("deposit")
    @Expose
    private Integer deposit;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getDepositFmt() {
        return depositFmt;
    }

    public void setDepositFmt(String depositFmt) {
        this.depositFmt = depositFmt;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

}

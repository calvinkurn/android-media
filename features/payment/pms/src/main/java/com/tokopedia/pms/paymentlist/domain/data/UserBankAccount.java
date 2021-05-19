
package com.tokopedia.pms.paymentlist.domain.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserBankAccount {

    @SerializedName("acc_no")
    @Expose
    private String accNo;
    @SerializedName("acc_name")
    @Expose
    private String accName;
    @SerializedName("bank_id")
    @Expose
    private int bankId;

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }
}

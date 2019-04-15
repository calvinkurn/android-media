package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class GqlCompleteTransactionResponse {
    @SerializedName("allDepositHistory")
    private
    DepositActivityResponse allDepositHistory;

    public DepositActivityResponse getAllDepositHistory() {
        return allDepositHistory;
    }

    public void setAllDepositHistory(DepositActivityResponse allDepositHistory) {
        this.allDepositHistory = allDepositHistory;
    }

}

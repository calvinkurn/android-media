package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class GqlBuyerDepositSummaryResponse {

    @SerializedName("buyerDepositHistory")
    private
    DepositActivityResponse buyerDepositHistory;

    public DepositActivityResponse getBuyerDepositHistory() {
        return buyerDepositHistory;
    }

    public void setBuyerDepositHistory(DepositActivityResponse buyerDepositHistory) {
        this.buyerDepositHistory = buyerDepositHistory;
    }

}

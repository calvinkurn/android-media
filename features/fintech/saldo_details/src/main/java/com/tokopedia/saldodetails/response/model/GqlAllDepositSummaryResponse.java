package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class GqlAllDepositSummaryResponse {

    @SerializedName("allDepositHistory")
    private
    DepositActivityResponse allDepositHistory;

    @SerializedName("buyerDepositHistory")
    private
    DepositActivityResponse buyerDepositHistory;

    @SerializedName("sellerDepositHistory")
    private
    DepositActivityResponse sellerDepositHistory;

    public DepositActivityResponse getAllDepositHistory() {
        return allDepositHistory;
    }

    public void setAllDepositHistory(DepositActivityResponse allDepositHistory) {
        this.allDepositHistory = allDepositHistory;
    }

    public DepositActivityResponse getBuyerDepositHistory() {
        return buyerDepositHistory;
    }

    public void setBuyerDepositHistory(DepositActivityResponse buyerDepositHistory) {
        this.buyerDepositHistory = buyerDepositHistory;
    }

    public DepositActivityResponse getSellerDepositHistory() {
        return sellerDepositHistory;
    }

    public void setSellerDepositHistory(DepositActivityResponse sellerDepositHistory) {
        this.sellerDepositHistory = sellerDepositHistory;
    }

}

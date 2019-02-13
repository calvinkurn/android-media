package com.tokopedia.saldodetails.response.model;

        import com.google.gson.annotations.SerializedName;

public class GqlWithdrawalTickerResponse {

    @SerializedName("withdrawalTicker")
    private WithdrawalTicker withdrawalTicker;

    public void setWithdrawalTicker(WithdrawalTicker withdrawalTicker) {
        this.withdrawalTicker = withdrawalTicker;
    }

    public WithdrawalTicker getWithdrawalTicker() {
        return withdrawalTicker;
    }
}

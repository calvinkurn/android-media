package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class WithdrawalTicker {

    @SerializedName("Ticker")
    private String tickerMessage;

    public String getTickerMessage() {
        return tickerMessage;
    }

    public void setTickerMessage(String tickerMessage) {
        this.tickerMessage = tickerMessage;
    }
}

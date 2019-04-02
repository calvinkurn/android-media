package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TickerBase {
    @Expose
    @SerializedName("tickerList")
    private List<TickerContainer> tickers;

    public List<TickerContainer> getTickers() {
        return tickers;
    }

    public void setTickers(List<TickerContainer> tickers) {
        this.tickers = tickers;
    }

    @Override
    public String toString() {
        return "TickerBase{" +
                "tickers=" + tickers +
                '}';
    }
}

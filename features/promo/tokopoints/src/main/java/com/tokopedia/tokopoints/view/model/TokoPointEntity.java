package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointEntity {
    @Expose
    @SerializedName("resultStatus")
    private ResultStatusEntity resultStatus;

    @Expose
    @SerializedName("status")
    private TokoPointStatusEntity status;

    @Expose
    @SerializedName("ticker")
    private TickerBase ticker;

    @Expose
    @SerializedName("sheetHowToGet")
    LobDetails lobs;

    public LobDetails getLobs() {
        return lobs;
    }

    public void setLobs(LobDetails lobs) {
        this.lobs = lobs;
    }

    public TickerBase getTicker() {
        return ticker;
    }

    public void setTicker(TickerBase ticker) {
        this.ticker = ticker;
    }

    public ResultStatusEntity getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusEntity resultStatus) {
        this.resultStatus = resultStatus;
    }

    public TokoPointStatusEntity getStatus() {
        return status;
    }

    public void setStatus(TokoPointStatusEntity status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TokoPointEntity{" +
                "resultStatus=" + resultStatus +
                ", status=" + status +
                ", ticker=" + ticker +
                ", lobs=" + lobs +
                '}';
    }
}

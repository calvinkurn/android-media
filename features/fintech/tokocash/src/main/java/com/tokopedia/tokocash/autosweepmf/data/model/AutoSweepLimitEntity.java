package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.SerializedName;

public class AutoSweepLimitEntity {
    @SerializedName("status")
    private boolean status;
    @SerializedName("show_autosweep")
    private int showAutoSweep;
    @SerializedName("autosweep")
    private int autoSweep;
    @SerializedName("amount_limit")
    private long amountLimit;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getShowAutoSweep() {
        return showAutoSweep;
    }

    public void setShowAutoSweep(int showAutoSweep) {
        this.showAutoSweep = showAutoSweep;
    }

    public int getAutoSweep() {
        return autoSweep;
    }

    public void setAutoSweep(int autoSweep) {
        this.autoSweep = autoSweep;
    }

    public long getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(long amountLimit) {
        this.amountLimit = amountLimit;
    }

    @Override
    public String toString() {
        return "AutoSweepLimitEntity{" +
                "status=" + status +
                ", showAutoSweep=" + showAutoSweep +
                ", autoSweep=" + autoSweep +
                ", amountLimit=" + amountLimit +
                '}';
    }
}

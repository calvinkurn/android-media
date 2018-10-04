
package com.tokopedia.gm.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GMFeaturedCashBackDetail {

    @SerializedName("cashback_status")
    @Expose
    private long cashbackStatus;
    @SerializedName("cashback_percent")
    @Expose
    private double cashbackPercent;
    @SerializedName("is_cashback_expired")
    @Expose
    private long isCashbackExpired;
    @SerializedName("cashback_value")
    @Expose
    private long cashbackValue;

    public long getCashbackStatus() {
        return cashbackStatus;
    }

    public void setCashbackStatus(long cashbackStatus) {
        this.cashbackStatus = cashbackStatus;
    }

    public double getCashbackPercent() {
        return cashbackPercent;
    }

    public void setCashbackPercent(double cashbackPercent) {
        this.cashbackPercent = cashbackPercent;
    }

    public long getIsCashbackExpired() {
        return isCashbackExpired;
    }

    public void setIsCashbackExpired(long isCashbackExpired) {
        this.isCashbackExpired = isCashbackExpired;
    }

    public long getCashbackValue() {
        return cashbackValue;
    }

    public void setCashbackValue(long cashbackValue) {
        this.cashbackValue = cashbackValue;
    }

}

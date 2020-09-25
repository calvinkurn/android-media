
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

    public double getCashbackPercent() {
        return cashbackPercent;
    }

}

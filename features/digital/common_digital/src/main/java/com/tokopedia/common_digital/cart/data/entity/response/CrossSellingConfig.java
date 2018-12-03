package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrossSellingConfig {
    @SerializedName("is_skipable")
    @Expose
    private boolean skipAble;

    @SerializedName("wording")
    @Expose
    private CrossSellingWording wording;

    public boolean isSkipAble() {
        return skipAble;
    }

    public CrossSellingWording getWording() {
        return wording;
    }
}

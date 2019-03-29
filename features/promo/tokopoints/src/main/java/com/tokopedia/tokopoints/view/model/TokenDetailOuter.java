package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDetailOuter {
    @Expose
    @SerializedName("luckyegg")
    private LuckyEggEntity tokenDetail;

    public LuckyEggEntity getTokenDetail() {
        return tokenDetail;
    }

    public void setTokenDetail(LuckyEggEntity tokenDetail) {
        this.tokenDetail = tokenDetail;
    }

    @Override
    public String toString() {
        return "TokenDetailOuter{" +
                "tokenDetail=" + tokenDetail +
                '}';
    }
}

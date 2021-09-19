package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.SerializedName;

public class Flags {
    @SerializedName("isOrderTradeIn")
    private boolean isOrderTradeIn;

    public boolean isIsOrderTradeIn() {
        return isOrderTradeIn;
    }
}
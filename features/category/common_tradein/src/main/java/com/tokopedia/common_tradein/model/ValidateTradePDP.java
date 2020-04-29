package com.tokopedia.common_tradein.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.common_tradein.model.ValidateTradeInResponse;

public class ValidateTradePDP {
    @SerializedName("validateTradeInPDP")
    private ValidateTradeInResponse response;

    public ValidateTradeInResponse getResponse() {
        return response;
    }

    public void setResponse(ValidateTradeInResponse response) {
        this.response = response;
    }
}

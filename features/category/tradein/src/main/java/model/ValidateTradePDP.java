package model;

import com.google.gson.annotations.SerializedName;

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

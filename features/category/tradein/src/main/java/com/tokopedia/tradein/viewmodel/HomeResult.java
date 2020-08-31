package com.tokopedia.tradein.viewmodel;

public class HomeResult {
    String displayMessage;
    String deviceDisplayName;
    boolean isSuccess;
    PriceState priceStatus;
    public Integer minPrice;
    public Integer maxPrice;

    public String getDisplayMessage() {
        return displayMessage;
    }

    public String getDeviceDisplayName() {
        return deviceDisplayName;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public PriceState getPriceStatus() {
        return priceStatus;
    }

    public enum PriceState {
        DIAGNOSED_VALID,
        DIAGNOSED_INVALID,
        NOT_DIAGNOSED,
        MONEYIN_ERROR
    }
}

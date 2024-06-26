package com.tokopedia.common_tradein.model;

import com.google.gson.annotations.SerializedName;

public class DeviceDiagGQL {
    @SerializedName("getTradeInDeviceDiagV2")
    private DeviceDataResponse diagResponse;

    public DeviceDataResponse getDiagResponse() {
        return diagResponse;
    }

    public void setDiagResponse(DeviceDataResponse diagResponse) {
        this.diagResponse = diagResponse;
    }
}

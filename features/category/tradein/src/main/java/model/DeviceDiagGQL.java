package model;

import com.google.gson.annotations.SerializedName;

public class DeviceDiagGQL {
    @SerializedName("getTradeInDeviceDiag")
    private DeviceDataResponse diagResponse;

    public DeviceDataResponse getDiagResponse() {
        return diagResponse;
    }

    public void setDiagResponse(DeviceDataResponse diagResponse) {
        this.diagResponse = diagResponse;
    }
}

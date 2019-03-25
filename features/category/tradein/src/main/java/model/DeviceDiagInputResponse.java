package model;

import com.google.gson.annotations.SerializedName;

public class DeviceDiagInputResponse {
    @SerializedName("insertTradeInDeviceDiag")
    private ValidateTradeInResponse deviceDiagInputRepsponse;

    public ValidateTradeInResponse getDeviceDiagInputRepsponse() {
        return deviceDiagInputRepsponse;
    }

    public void setDeviceDiagInputRepsponse(ValidateTradeInResponse deviceDiagInputRepsponse) {
        this.deviceDiagInputRepsponse = deviceDiagInputRepsponse;
    }
}

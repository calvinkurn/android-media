package com.tokopedia.common_tradein.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.common_tradein.model.ValidateTradeInResponse;

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

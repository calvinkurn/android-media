package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderPriorityStaticMessage {

    @SerializedName("duration_message")
    @Expose
    public String durationMessage;

    @SerializedName("checkbox_message")
    @Expose
    public String checkboxMessage;

    @SerializedName("warningbox_message")
    @Expose
    public String warningBoxMessage;

    @SerializedName("fee_message")
    @Expose
    public String feeMessage;

    @SerializedName("pdp_message")
    @Expose
    public String pdpMessage;


    public String getDurationMessage() {
        return durationMessage;
    }

    public void setDurationMessage(String durationMessage) {
        this.durationMessage = durationMessage;
    }

    public String getCheckboxMessage() {
        return checkboxMessage;
    }

    public void setCheckboxMessage(String checkboxMessage) {
        this.checkboxMessage = checkboxMessage;
    }

    public String getWarningBoxMessage() {
        return warningBoxMessage;
    }

    public void setWarningBoxMessage(String warningBoxMessage) {
        this.warningBoxMessage = warningBoxMessage;
    }

    public String getFeeMessage() {
        return feeMessage;
    }

    public void setFeeMessage(String feeMessage) {
        this.feeMessage = feeMessage;
    }

    public String getPdpMessage() {
        return pdpMessage;
    }

    public void setPdpMessage(String pdpMessage) {
        this.pdpMessage = pdpMessage;
    }
}

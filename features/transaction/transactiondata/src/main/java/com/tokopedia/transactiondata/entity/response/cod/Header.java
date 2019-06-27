package com.tokopedia.transactiondata.entity.response.cod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 19/12/18.
 */
public class Header {

    @SerializedName("process_time")
    @Expose
    private Double processTime;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("error_code")
    @Expose
    private String errorCode;

    public Double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Double processTime) {
        this.processTime = processTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

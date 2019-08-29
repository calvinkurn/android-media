package com.tokopedia.transactiondata.entity.response.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CheckoutDataResponse {
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("error_reporter")
    @Expose
    private ErrorReporterResponse errorReporterResponse;

    public int getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public ErrorReporterResponse getErrorReporterResponse() {
        return errorReporterResponse;
    }
}

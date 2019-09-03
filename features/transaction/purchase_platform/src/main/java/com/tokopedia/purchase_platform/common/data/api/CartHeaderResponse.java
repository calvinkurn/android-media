package com.tokopedia.purchase_platform.common.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartHeaderResponse {
    @SerializedName("process_time")
    @Expose
    private double processTime;
    @SerializedName("messages")
    @Expose
    private List<String> message = new ArrayList<>();
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("error_code")
    @Expose
    private String errorCode;

    public double getProcessTime() {
        return processTime;
    }

    public List<String> getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessageFormatted() {
        if (message == null || message.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < message.size(); i++) {
            stringBuilder.append(message.get(i));
            if (i < message.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString().trim();
    }
}

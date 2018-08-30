package com.tokopedia.reksadana.view.data.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @Expose
    @SerializedName("code")
    private String code;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("success")
    private boolean success;

    public Result(String code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public boolean success() {
        return success;
    }

}

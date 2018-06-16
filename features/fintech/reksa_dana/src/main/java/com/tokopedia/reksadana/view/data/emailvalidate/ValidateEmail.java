package com.tokopedia.reksadana.view.data.emailvalidate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.reksadana.view.data.common.Result;

public class ValidateEmail {
    @Expose
    @SerializedName("result")
    private Result result;
    @Expose
    @SerializedName("valid")
    private boolean valid;
    @Expose
    @SerializedName("message")
    private String message;

    public ValidateEmail(Result result, boolean valid, String message) {
        this.result = result;
        this.valid = valid;
        this.message = message;
    }
    public Result result(){
        return result;
    }
    public boolean valid(){
        return valid;
    }
    public String message(){
        return message;
    }

    @Override
    public String toString() {
        return "ValidateEmail{" +
                "result=" + result +
                ", valid=" + valid +
                ", message='" + message +
                '}';
    }
}

package com.tokopedia.core.product.model.productotherace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 19/01/2016.
 */

@Deprecated
public class Status {
    private static final String TAG = Status.class.getSimpleName();

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

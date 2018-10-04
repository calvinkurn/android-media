package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultStatusEntity {
    @Expose
    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResultStatusEntity{" +
                "code=" + code +
                '}';
    }
}

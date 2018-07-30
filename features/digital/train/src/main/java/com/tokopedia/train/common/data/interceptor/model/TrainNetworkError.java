package com.tokopedia.train.common.data.interceptor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainNetworkError {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("code")
    @Expose
    private String code;

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }
}

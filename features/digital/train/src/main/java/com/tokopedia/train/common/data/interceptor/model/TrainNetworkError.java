package com.tokopedia.train.common.data.interceptor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainNetworkError {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("path")
    @Expose
    private List<String> paths;

    public String getMessage() {
        return message;
    }

    public List<String> getPaths() {
        return paths;
    }
}

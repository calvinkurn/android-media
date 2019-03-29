package com.tokopedia.posapp.react.datasource.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 8/28/17.
 */

public class CacheResult<T> {
    @SerializedName("data")
    @Expose
    private T data;

    @SerializedName("message")
    @Expose
    private String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

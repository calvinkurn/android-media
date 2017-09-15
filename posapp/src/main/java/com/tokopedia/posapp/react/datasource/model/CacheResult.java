package com.tokopedia.posapp.react.datasource.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 8/28/17.
 */

public class CacheResult<T> {
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

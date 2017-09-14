package com.tokopedia.posapp.react.datasource.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 8/28/17.
 */

public class CacheResult<T> {
    public List<T> datas;

    @SerializedName("data")
    public T data;
}

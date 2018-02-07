package com.tokopedia.abstraction.common.data.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/8/2017.
 */

public class DataRequest<T> {
    @SerializedName("data")
    private T data;

    public DataRequest(){

    }
    public DataRequest(T data){
        this.data = data;
    }
}

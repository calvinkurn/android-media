package com.tokopedia.tkpd.thankyou.data.pojo.digital;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 12/8/17.
 */

public class DigitalDataWrapper<T> {
    @SerializedName("data")
    @Expose
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

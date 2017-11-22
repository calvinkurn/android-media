package com.tokopedia.digital.widget.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/11/17.
 */

public class ResponseMetaFavoriteNumber {
    @SerializedName("default_index")
    @Expose
    private int defaultIndex;

    public int getDefaultIndex() {
        return defaultIndex;
    }

    @Override
    public String toString() {
        return "DEFAULT INDEX = " + String.valueOf(defaultIndex);
    }
}

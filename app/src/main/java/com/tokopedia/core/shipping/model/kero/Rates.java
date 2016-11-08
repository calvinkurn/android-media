package com.tokopedia.core.shipping.model.kero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Herdi_WORK on 22.09.16.
 */

public class Rates {
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    public boolean isNullData(){
        return data == null;
    }
}

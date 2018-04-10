package com.tokopedia.digital_deals.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealsResponseEntity {

    @SerializedName("data")
    @Expose
    private DealsDataResponseEntity data;

    public DealsDataResponseEntity getData() {
        return data;
    }

    public void setData(DealsDataResponseEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}

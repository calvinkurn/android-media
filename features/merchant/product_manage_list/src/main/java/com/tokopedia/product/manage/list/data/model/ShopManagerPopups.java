package com.tokopedia.product.manage.list.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopManagerPopups {
    @SerializedName("data")
    @Expose
    PopupsData popupsData;

    public PopupsData getPopupsData() {
        return popupsData;
    }

    public void setPopupsData(PopupsData popupsData) {
        this.popupsData = popupsData;
    }
}

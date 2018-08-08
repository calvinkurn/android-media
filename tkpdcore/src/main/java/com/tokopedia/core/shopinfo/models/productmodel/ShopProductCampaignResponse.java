package com.tokopedia.core.shopinfo.models.productmodel;

import android.util.Pair;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by brilliant.oka on 15/05/17.
 */

public class ShopProductCampaignResponse {

    @SerializedName("data")
    @Expose
    private java.util.List<ShopProductCampaign> data;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("process-time")
    @Expose
    private String processTime;

    public List<ShopProductCampaign> getData() {
        return data;
    }

    public void setData(List<ShopProductCampaign> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
}

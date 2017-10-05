package com.tokopedia.core.product.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brilliant.oka on 13/04/17.
 */

public class ProductCampaignResponse {
    @SerializedName("data")
    @Expose
    private ProductCampaign data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("process-time")
    @Expose
    private String processTime;

    public ProductCampaign getData() {
        return data;
    }

    public void setData(ProductCampaign data) {
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

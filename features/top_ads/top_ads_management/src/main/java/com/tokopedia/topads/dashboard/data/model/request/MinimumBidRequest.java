package com.tokopedia.topads.dashboard.data.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author errysuprayogi on 25,March,2019
 */
public class MinimumBidRequest {

    @SerializedName("requestType")
    private String requestType;
    @SerializedName("shopId")
    private int shopId;
    @SerializedName("source")
    private String source;
    @SerializedName("dataSuggestions")
    private List<DataSuggestions> dataSuggestions;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<DataSuggestions> getDataSuggestions() {
        return dataSuggestions;
    }

    public void setDataSuggestions(List<DataSuggestions> dataSuggestions) {
        this.dataSuggestions = dataSuggestions;
    }

}

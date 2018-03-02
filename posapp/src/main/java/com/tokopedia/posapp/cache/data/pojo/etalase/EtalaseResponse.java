package com.tokopedia.posapp.cache.data.pojo.etalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 9/22/17.
 */

public class EtalaseResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private List<EtalaseItemResponse> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EtalaseItemResponse> getData() {
        return data;
    }

    public void setData(List<EtalaseItemResponse> data) {
        this.data = data;
    }
}

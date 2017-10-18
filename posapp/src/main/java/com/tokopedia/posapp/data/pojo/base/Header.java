package com.tokopedia.posapp.data.pojo.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.posapp.data.pojo.product.ShopDetail;

/**
 * Created by okasurya on 10/17/17.
 */

public class Header {
    @SerializedName("total_data")
    @Expose
    private int totalData;
    @SerializedName("process_time")
    @Expose
    private double processTime;
    @SerializedName("additioanl_params")
    @Expose
    private String additionalParams;

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }
}

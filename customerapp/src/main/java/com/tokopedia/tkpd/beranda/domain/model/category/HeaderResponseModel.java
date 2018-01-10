package com.tokopedia.tkpd.beranda.domain.model.category;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class HeaderResponseModel {

    @SerializedName("total_data")
    private int totalData;
    @SerializedName("process_time")
    private double processTime;

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
}

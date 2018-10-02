
package com.tokopedia.topads.dashboard.data.model.ticker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Header {

    @SerializedName("process_time")
    @Expose
    private double processTime;
    @SerializedName("total_data")
    @Expose
    private int totalData;

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

}

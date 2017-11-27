package com.tokopedia.tkpd.beranda.domain.model.toppicks;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class TopPicksResponseModel {

    @SerializedName("process_time")
    private double processTime;
    @SerializedName("data")
    private TopPicksDataModel data;

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public TopPicksDataModel getData() {
        return data;
    }

    public void setData(TopPicksDataModel data) {
        this.data = data;
    }

}

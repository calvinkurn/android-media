
package com.tokopedia.core.network.entity.topPicks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopPicksResponse {

    @SerializedName("process_time")
    @Expose
    private Float processTime;
    @SerializedName("data")
    @Expose
    private Data data;

    public Float getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Float processTime) {
        this.processTime = processTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}

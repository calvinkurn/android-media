package com.tokopedia.core.network.entity.variant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoshua on 27/03/18.
 */

public class ProductStockResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Child data;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Child getData() {
        return data;
    }

    public void setData(Child data) {
        this.data = data;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }
}

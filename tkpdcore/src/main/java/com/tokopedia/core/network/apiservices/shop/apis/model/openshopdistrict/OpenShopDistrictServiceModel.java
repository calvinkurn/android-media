
package com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenShopDistrictServiceModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

}

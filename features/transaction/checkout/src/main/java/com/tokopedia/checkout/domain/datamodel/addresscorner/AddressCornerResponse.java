
package com.tokopedia.checkout.domain.datamodel.addresscorner;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressCornerResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("tokopedia_corner_data")
    @Expose
    private List<TokopediaCornerDatum> tokopediaCornerData = null;

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

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public List<TokopediaCornerDatum> getTokopediaCornerData() {
        return tokopediaCornerData;
    }

    public void setTokopediaCornerData(List<TokopediaCornerDatum> tokopediaCornerData) {
        this.tokopediaCornerData = tokopediaCornerData;
    }

}

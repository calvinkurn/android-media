package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by m.normansyah on 26/11/2015.
 */
public class ProductFeedData {
    @SerializedName("config")
    @Expose
    String config;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;
    @SerializedName("data")
    @Expose
    ProductItemData data;
    @SerializedName("message_error")
    @Expose
    List<String> messageError;


    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public ProductItemData getData() {
        return data;
    }

    public void setData(ProductItemData data) {
        this.data = data;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    @Override
    public String toString() {
        return "ProductFeedData{" +
                "config='" + config + '\'' +
                ", status='" + status + '\'' +
                ", serverProcessTime='" + serverProcessTime + '\'' +
                ", data=" + data +
                ", messageError=" + messageError +
                '}';
    }
}

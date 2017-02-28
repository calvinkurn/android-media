package com.tokopedia.tkpd.home.feed.data.source.cloud.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.home.ProductItemData;

import java.util.List;

@SuppressWarnings("unused")
public class RecentProductResponse {
    @SerializedName("config")
    @Expose
    private String config;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("data")
    @Expose
    private ProductItemData data;
    @SerializedName("message_error")
    @Expose
    private List<String> messageError;


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
        return "RecentProductResponse{" +
                "config='" + config + '\'' +
                ", status='" + status + '\'' +
                ", serverProcessTime='" + serverProcessTime + '\'' +
                ", data=" + data +
                ", messageError=" + messageError +
                '}';
    }
}

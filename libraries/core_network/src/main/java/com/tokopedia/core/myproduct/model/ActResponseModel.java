package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Toped18 on 6/9/2016.
 */
public class ActResponseModel {

    @SerializedName("status")
    String status;

    @SerializedName("message_error")
    String messageError[];

    @SerializedName("message_status")
    String messageStatus[];

    @SerializedName("config")
    String config;

    @SerializedName("server_process_time")
    String serverProcessTime;

    @SerializedName("result")
    ActResponseModelData actResponseModelData;

    public ActResponseModelData getActResponseModelData() {
        return actResponseModelData;
    }

    public void setActResponseModelData(ActResponseModelData actResponseModelData) {
        this.actResponseModelData = actResponseModelData;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String[] getMessageError() {
        return messageError;
    }

    public void setMessageError(String[] messageError) {
        this.messageError = messageError;
    }

    public String[] getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String[] messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.tokopedia.tkpd.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorMessageModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("message_error")
    @Expose
    private List<String> messageError = null;

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

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

}

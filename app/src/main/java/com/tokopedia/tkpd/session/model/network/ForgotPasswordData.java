package com.tokopedia.tkpd.session.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.session.model.ForgotPasswordModel;

import java.util.List;

/**
 * Created by m.normansyah on 25/11/2015.
 */
public class ForgotPasswordData {
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
    ForgotPasswordModel forgotPasswordModel;
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

    public ForgotPasswordModel getForgotPasswordModel() {
        return forgotPasswordModel;
    }

    public void setForgotPasswordModel(ForgotPasswordModel forgotPasswordModel) {
        this.forgotPasswordModel = forgotPasswordModel;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    @Override
    public String toString() {
        return "ForgotPasswordData{" +
                "config='" + config + '\'' +
                ", status='" + status + '\'' +
                ", serverProcessTime='" + serverProcessTime + '\'' +
                ", forgotPasswordModel=" + forgotPasswordModel +
                ", messageError=" + messageError +
                '}';
    }
}

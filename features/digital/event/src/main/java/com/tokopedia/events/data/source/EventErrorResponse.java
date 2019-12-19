package com.tokopedia.events.data.source;

import java.io.IOException;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;

public class EventErrorResponse extends BaseResponseError {

    @SerializedName("server_process_time")
    private String serverProcessTime;

    @SerializedName("message_error")
    private List<String> messageError;

    @SerializedName("config")
    private Object config;

    @SerializedName("status")
    private String status;


    public void setServerProcessTime(String serverProcessTime){
        this.serverProcessTime = serverProcessTime;
    }

    public String getServerProcessTime(){
        return serverProcessTime;
    }

    public void setMessageError(List<String> messageError){
        this.messageError = messageError;
    }

    public List<String> getMessageError(){
        return messageError;
    }

    public void setConfig(Object config){
        this.config = config;
    }

    public Object getConfig(){
        return config;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return
                "EventErrorResponse{" +
                        ",server_process_time = '" + serverProcessTime + '\'' +
                        ",message_error = '" + messageError + '\'' +
                        ",config = '" + config + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }

    @Override
    public String getErrorKey() {
        return "message_error";
    }

    @Override
    public boolean hasBody() {
        return messageError != null;
    }

    @Override
    public IOException createException() {
        return new EventException(messageError.get(0));
    }
}
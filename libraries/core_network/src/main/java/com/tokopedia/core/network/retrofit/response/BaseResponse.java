package com.tokopedia.core.network.retrofit.response;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 01/12/2015.
 */
public class BaseResponse {
    private static final String TAG = BaseResponse.class.getSimpleName();

    @SerializedName("message_error")
    @Expose
    private List<String> messageError = new ArrayList<String>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("data")
    @Expose
    private JsonElement data;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;

    public List<String> getMessageError() {
        return messageError;
    }

    public String getStatus() {
        return status;
    }

    public Object getConfig() {
        return config;
    }

    public JsonElement getData() {
        return data;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }
}

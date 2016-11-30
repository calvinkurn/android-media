package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by m.normansyah on 27/11/2015.
 */
public class FavoriteSendData {
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
    Result result;
    @SerializedName("message_error")
    @Expose
    List<String> messageError;

    static class Result{
        @SerializedName("content")
        @Expose
        String content;
        @SerializedName("is_success")
        @Expose
        int isSuccess;

        public static final int SUCCESS = 1;
    }
}

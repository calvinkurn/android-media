package com.tokopedia.search.result.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.search.result.domain.model.SearchShopModel;

public class SearchShopResponse {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("config")
    @Expose
    Config config;

    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;

    @SerializedName("result")
    @Expose
    public SearchShopModel searchShopModel;

    public static class Config {
        @SerializedName("backoff_multi")
        @Expose
        String backoffMulti;

        @SerializedName("timeout")
        @Expose
        String timeout;

        @SerializedName("max_retries")
        @Expose
        String maxRetries;
    }
}

package com.tokopedia.search.result.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;

public class SearchCatalogResponse {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("config")
    @Expose
    public Config config;

    @SerializedName("server_process_time")
    @Expose
    public String serverProcessTime;

    @SerializedName("data")
    @Expose
    public SearchCatalogModel result;

    public static class Config {
        @SerializedName("backoff_multi")
        @Expose
        String backoffMulti;

        @SerializedName("max_retries")
        @Expose
        String maxRetries;

        @SerializedName("timeout")
        @Expose
        String timeout;
    }
}

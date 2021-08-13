package com.tokopedia.logger.utils;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataLogConfig {

    @SerializedName("enabled")
    @Expose
    private boolean enabled = false;

    @SerializedName("appVersionMin")
    @Expose
    private long appVersionMin = 0L;

    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<>();

    @SerializedName("client_log")
    @Expose
    private List<String> clientLogs = new ArrayList<>(Arrays.asList(Constants.CLIENT_SCALYR));

    @SerializedName("query_limits")
    @Expose
    private List<Integer> queryLimits = new ArrayList<>(Arrays.asList(50, 50));

    public long getAppVersionMin() {
        return appVersionMin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Nullable
    public List<String> getTags() {
        return tags;
    }

    @Nullable
    public List<String> getClientLogs() {
        return clientLogs;
    }

    @Nullable
    public List<Integer> getQueryLimits() {
        return queryLimits;
    }
}

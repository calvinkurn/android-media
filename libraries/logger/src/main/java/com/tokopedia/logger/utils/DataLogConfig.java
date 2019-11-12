package com.tokopedia.logger.utils;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;

public class DataLogConfig {

    @SerializedName("enabled")
    @Expose
    private boolean enabled;

    @SerializedName("appVersionMin")
    @Expose
    private long appVersionMin;

    @SerializedName("tags")
    @Expose
    private List<String> tags;

    public long getAppVersionMin() {
        return appVersionMin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Nullable
    public List<String> getTags() { return tags; }
}

package com.tokopedia.tkpd.timber;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLogConfig {

    @SerializedName("appVersionMin")
    @Expose
    private long appVersionMin;

    @SerializedName("enabled")
    @Expose
    private boolean enabled;

    public long getAppVersionMin() {
        return appVersionMin;
    }

    public boolean isEnabled() {
        return enabled;
    }
}

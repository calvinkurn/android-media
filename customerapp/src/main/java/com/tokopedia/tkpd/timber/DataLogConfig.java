package com.tokopedia.tkpd.timber;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataLogConfig {

    @SerializedName("appVersionMin")
    @Expose
    private long appVersionMin;

    @SerializedName("enabled")
    @Expose
    private boolean enabled;

    @SerializedName("priority")
    @Expose
    private List<Boolean> priorityList;

    public long getAppVersionMin() {
        return appVersionMin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Nullable
    public List<Boolean> getPriorityList() {
        return priorityList;
    }
}

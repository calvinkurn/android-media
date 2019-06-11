package com.tokopedia.tkpd.timber;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLogConfig {

    @SerializedName("appVersionMin")
    @Expose
    private int appVersionMin;

    public int getAppVersionMin() {
        return appVersionMin;
    }

    public void setAppVersionMin(int appVersionMin) {
        this.appVersionMin = appVersionMin;
    }
}

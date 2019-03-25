
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usage {

    @SerializedName("activeCountdown")
    @Expose
    private Integer activeCountdown;
    @SerializedName("expiredCountdown")
    @Expose
    private Integer expiredCountdown;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("usageStr")
    @Expose
    private String usageStr;

    public Integer getActiveCountdown() {
        return activeCountdown;
    }

    public void setActiveCountdown(Integer activeCountdown) {
        this.activeCountdown = activeCountdown;
    }

    public Integer getExpiredCountdown() {
        return expiredCountdown;
    }

    public void setExpiredCountdown(Integer expiredCountdown) {
        this.expiredCountdown = expiredCountdown;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsageStr() {
        return usageStr;
    }

    public void setUsageStr(String usageStr) {
        this.usageStr = usageStr;
    }

}

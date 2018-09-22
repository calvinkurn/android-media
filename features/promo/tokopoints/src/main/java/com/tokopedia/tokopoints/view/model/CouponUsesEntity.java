package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponUsesEntity {
    @Expose
    @SerializedName(value = "activeCountdown", alternate = {"active_count_down"})
    private long activeCountDown;

    @Expose
    @SerializedName(value = "buttonUsage", alternate = {"btn_usage"})
    private CouponButtonUsageEntity btnUsage;

    @Expose
    @SerializedName(value = "expiredCountdown", alternate = {"expired_count_down"})
    private long expiredCountDown;

    @Expose
    @SerializedName("text")
    private String text;

    @Expose
    @SerializedName(value = "usageStr", alternate = {"usage_str"})
    private String usageStr;

    public long getActiveCountDown() {
        return activeCountDown;
    }

    public void setActiveCountDown(long activeCountDown) {
        this.activeCountDown = activeCountDown;
    }

    public CouponButtonUsageEntity getBtnUsage() {
        return btnUsage;
    }

    public void setBtnUsage(CouponButtonUsageEntity btnUsage) {
        this.btnUsage = btnUsage;
    }

    public long getExpiredCountDown() {
        return expiredCountDown;
    }

    public void setExpiredCountDown(long expiredCountDown) {
        this.expiredCountDown = expiredCountDown;
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

    @Override
    public String toString() {
        return "CouponUsesEntity{" +
                "activeCountDown=" + activeCountDown +
                ", btnUsage=" + btnUsage +
                ", expiredCountDown=" + expiredCountDown +
                ", text='" + text + '\'' +
                ", usageStr='" + usageStr + '\'' +
                '}';
    }
}

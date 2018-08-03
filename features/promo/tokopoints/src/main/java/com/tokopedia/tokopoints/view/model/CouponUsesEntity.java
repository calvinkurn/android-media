package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponUsesEntity {
    @Expose
    @SerializedName("activeCountdown")
    private int activeCountDown;

    @Expose
    @SerializedName("buttonUsage")
    private CouponButtonUsageEntity btnUsage;

    @Expose
    @SerializedName("expiredCountdown")
    private int expiredCountDown;

    @Expose
    @SerializedName("text")
    private String text;

    @Expose
    @SerializedName("usageStr")
    private String usageStr;

    public int getActiveCountDown() {
        return activeCountDown;
    }

    public void setActiveCountDown(int activeCountDown) {
        this.activeCountDown = activeCountDown;
    }

    public CouponButtonUsageEntity getBtnUsage() {
        return btnUsage;
    }

    public void setBtnUsage(CouponButtonUsageEntity btnUsage) {
        this.btnUsage = btnUsage;
    }

    public int getExpiredCountDown() {
        return expiredCountDown;
    }

    public void setExpiredCountDown(int expiredCountDown) {
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


package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountdownAttr {

    @SerializedName("showTimer")
    @Expose
    private Boolean showTimer;
    @SerializedName("activeCountDown")
    @Expose
    private Integer activeCountDown;
    @SerializedName("expiredCountDown")
    @Expose
    private Integer expiredCountDown;

    public Boolean getShowTimer() {
        return showTimer;
    }

    public void setShowTimer(Boolean showTimer) {
        this.showTimer = showTimer;
    }

    public Integer getActiveCountDown() {
        return activeCountDown;
    }

    public void setActiveCountDown(Integer activeCountDown) {
        this.activeCountDown = activeCountDown;
    }

    public Integer getExpiredCountDown() {
        return expiredCountDown;
    }

    public void setExpiredCountDown(Integer expiredCountDown) {
        this.expiredCountDown = expiredCountDown;
    }

}

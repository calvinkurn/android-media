
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountdownAttr {

    @SerializedName("showTimer")
    @Expose
    private boolean showTimer;

    @SerializedName("expiredCountDown")
    @Expose
    private long expiredCountDown;

    public boolean isShowTimer() {
        return showTimer;
    }

    public void setShowTimer(boolean showTimer) {
        this.showTimer = showTimer;
    }


    public long getExpiredCountDown() {
        return expiredCountDown;
    }

    public void setExpiredCountDown(long expiredCountDown) {
        this.expiredCountDown = expiredCountDown;
    }
}

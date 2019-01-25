package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meta on 18/01/19.
 */
public class HomeFlagModel {

    @SerializedName("isOSBottomNav")
    @Expose
    private boolean isOSBottomNav;

    public boolean isOSBottomNav() {
        return isOSBottomNav;
    }

    public void setOSBottomNav(boolean OSBottomNav) {
        isOSBottomNav = OSBottomNav;
    }
}

package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 8/28/18.
 */
public class ReputationShop {
    @SerializedName("badge")
    @Expose
    public String badge = "";
    @SerializedName("badge_hd")
    @Expose
    public String badgeHd = "";

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBadgeHd() {
        return badgeHd;
    }

    public void setBadgeHd(String badgeHd) {
        this.badgeHd = badgeHd;
    }
}

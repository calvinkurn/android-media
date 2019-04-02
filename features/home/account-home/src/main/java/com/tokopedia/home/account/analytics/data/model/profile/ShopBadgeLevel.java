
package com.tokopedia.home.account.analytics.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopBadgeLevel {

    @SerializedName("level")
    @Expose
    private Integer level = 0;
    @SerializedName("set")
    @Expose
    private Integer set = 0;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }
}

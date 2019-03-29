
package com.tokopedia.core.drawer2.data.pojo.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopBadgeLevel {

    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("set")
    @Expose
    private Integer set;

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

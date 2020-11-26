
package com.tokopedia.topads.common.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopBadgeLevel {

    @SerializedName("level")
    @Expose
    public int level;
    @SerializedName("set")
    @Expose
    public int set;

    public int getLevel() {
        return level;
    }

    public int getSet() {
        return set;
    }
}

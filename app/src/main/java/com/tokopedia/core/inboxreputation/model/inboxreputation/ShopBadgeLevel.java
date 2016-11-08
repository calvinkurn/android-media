
package com.tokopedia.core.inboxreputation.model.inboxreputation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@org.parceler.Parcel
public class ShopBadgeLevel{

    @SerializedName("level")
    @Expose
    int level;
    @SerializedName("set")
    @Expose
    int set;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}

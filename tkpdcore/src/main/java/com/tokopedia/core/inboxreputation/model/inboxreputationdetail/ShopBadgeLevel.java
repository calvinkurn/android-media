
package com.tokopedia.core.inboxreputation.model.inboxreputationdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@org.parceler.Parcel
public class ShopBadgeLevel {

    @SerializedName("level")
    @Expose
    int level;
    @SerializedName("set")
    @Expose
    int set;

    /**
     * @return The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return The set
     */
    public int getSet() {
        return set;
    }

    /**
     * @param set The set
     */
    public void setSet(int set) {
        this.set = set;
    }

}

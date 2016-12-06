
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopBadgeLevel {

    @SerializedName("level")
    @Expose
    public int level;
    @SerializedName("set")
    @Expose
    public int set;

}

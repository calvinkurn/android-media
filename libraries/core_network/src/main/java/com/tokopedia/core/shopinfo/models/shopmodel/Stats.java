
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Stats {

    @SerializedName("shop_badge_level")
    @Expose
    public ShopBadgeLevel shopBadgeLevel;
    @SerializedName("shop_reputation_score")
    @Expose
    public String shopReputationScore;
}

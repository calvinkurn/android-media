
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QualityWidth {

    @SerializedName("one_star_rank")
    @Expose
    public float oneStarRank;
    @SerializedName("four_star_rank")
    @Expose
    public float fourStarRank;
    @SerializedName("five_star_rank")
    @Expose
    public float fiveStarRank;
    @SerializedName("three_star_rank")
    @Expose
    public float threeStarRank;
    @SerializedName("two_star_rank")
    @Expose
    public float twoStarRank;

}


package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quality {

    @SerializedName("rating_star")
    @Expose
    public int ratingStar;
    @SerializedName("average")
    @Expose
    public String average;
    @SerializedName("one_star_rank")
    @Expose
    public String oneStarRank;
    @SerializedName("count_total")
    @Expose
    public String countTotal;
    @SerializedName("four_star_rank")
    @Expose
    public String fourStarRank;
    @SerializedName("five_star_rank")
    @Expose
    public String fiveStarRank;
    @SerializedName("three_star_rank")
    @Expose
    public String threeStarRank;
    @SerializedName("two_star_rank")
    @Expose
    public String twoStarRank;

}

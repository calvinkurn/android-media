
package com.tokopedia.topads.common.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ShopLastTwelveMonths {

    @SerializedName("count_score_good")
    @Expose
    public String countScoreGood;
    @SerializedName("count_score_bad")
    @Expose
    public String countScoreBad;
    @SerializedName("count_score_neutral")
    @Expose
    public String countScoreNeutral;

}

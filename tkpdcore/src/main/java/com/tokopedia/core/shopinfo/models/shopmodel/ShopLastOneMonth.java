
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopLastOneMonth {

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


package com.tokopedia.core.drawer2.data.pojo.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopLastOneMonth {

    @SerializedName("count_score_good")
    @Expose
    private String countScoreGood;
    @SerializedName("count_score_bad")
    @Expose
    private String countScoreBad;
    @SerializedName("count_score_neutral")
    @Expose
    private String countScoreNeutral;
}

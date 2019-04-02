
package com.tokopedia.home.account.analytics.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopLastSixMonths {

    @SerializedName("count_score_good")
    @Expose
    private String countScoreGood = "";
    @SerializedName("count_score_bad")
    @Expose
    private String countScoreBad = "";
    @SerializedName("count_score_neutral")
    @Expose
    private String countScoreNeutral = "";

    public String getCountScoreGood() {
        return countScoreGood;
    }

    public void setCountScoreGood(String countScoreGood) {
        this.countScoreGood = countScoreGood;
    }

    public String getCountScoreBad() {
        return countScoreBad;
    }

    public void setCountScoreBad(String countScoreBad) {
        this.countScoreBad = countScoreBad;
    }

    public String getCountScoreNeutral() {
        return countScoreNeutral;
    }

    public void setCountScoreNeutral(String countScoreNeutral) {
        this.countScoreNeutral = countScoreNeutral;
    }
}

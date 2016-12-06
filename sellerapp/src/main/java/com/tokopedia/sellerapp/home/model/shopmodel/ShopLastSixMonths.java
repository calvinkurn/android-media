
package com.tokopedia.sellerapp.home.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ShopLastSixMonths {

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

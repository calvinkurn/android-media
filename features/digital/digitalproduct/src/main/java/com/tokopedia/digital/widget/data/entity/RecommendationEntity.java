package com.tokopedia.digital.widget.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 13/11/18.
 */
public class RecommendationEntity {

    @SerializedName("rechargeFavoriteRecommendationList")
    @Expose
    private RechargeFavoriteRecommentaionList rechargeFavoriteRecommentaionList;

    public RechargeFavoriteRecommentaionList getRechargeFavoriteRecommentaionList() {
        return rechargeFavoriteRecommentaionList;
    }
}

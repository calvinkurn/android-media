package com.tokopedia.digital.widget.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 14/11/18.
 */
public class RechargeFavoriteRecommentaionList {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("recommendations")
    @Expose
    private List<RecommendationItemEntity> recommendationItemEntityList;

    public String getTitle() {
        return title;
    }

    public List<RecommendationItemEntity> getRecommendationItemEntityList() {
        return recommendationItemEntityList;
    }
}

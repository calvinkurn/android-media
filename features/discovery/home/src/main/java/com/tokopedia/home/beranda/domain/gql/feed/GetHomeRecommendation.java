
package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetHomeRecommendation {

    @SerializedName("recommendation_tabs")
    @Expose
    private List<RecommendationTab> recommendationTabs;

    @SerializedName("recommendation_product")
    @Expose
    private RecommendationProduct recommendationProduct;

    public RecommendationProduct getRecommendationProduct() {
        return recommendationProduct;
    }

    public List<RecommendationTab> getRecommendationTabs() {
        return recommendationTabs;
    }
}

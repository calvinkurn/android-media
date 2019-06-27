
package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetHomeRecommendationContent {

    @SerializedName("recommendation_product")
    @Expose
    private RecommendationProduct recommendationProduct;

    public RecommendationProduct getRecommendationProduct() {
        return recommendationProduct;
    }
}

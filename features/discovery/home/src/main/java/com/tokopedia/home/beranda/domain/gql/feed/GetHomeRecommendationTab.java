
package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetHomeRecommendationTab {

    @SerializedName("recommendation_tabs")
    @Expose
    private List<RecommendationTab> recommendationTabs;

    public List<RecommendationTab> getRecommendationTabs() {
        return recommendationTabs;
    }
}

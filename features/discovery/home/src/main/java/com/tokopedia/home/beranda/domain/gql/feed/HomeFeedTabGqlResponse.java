
package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeFeedTabGqlResponse {

    @SerializedName("get_home_recommendation")
    @Expose
    private GetHomeRecommendationTab homeRecommendation;

    public GetHomeRecommendationTab getHomeRecommendation() {
        return homeRecommendation;
    }
}

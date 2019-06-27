
package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeFeedContentGqlResponse {

    @SerializedName("get_home_recommendation")
    @Expose
    private GetHomeRecommendationContent homeRecommendation;

    public GetHomeRecommendationContent getHomeRecommendation() {
        return homeRecommendation;
    }
}

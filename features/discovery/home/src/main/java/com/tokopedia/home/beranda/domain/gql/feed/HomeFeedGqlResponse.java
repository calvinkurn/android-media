
package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeFeedGqlResponse {

    @SerializedName("get_home_recommendation")
    @Expose
    private GetHomeRecommendation homeRecommendation;

    public GetHomeRecommendation getHomeRecommendation() {
        return homeRecommendation;
    }
}


package com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUsernameSuggestionData {

    @SerializedName("bymeGetRecommendedAffiliateName")
    @Expose
    private BymeGetRecommendedAffiliateName suggestion;

    public BymeGetRecommendedAffiliateName getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(BymeGetRecommendedAffiliateName suggestion) {
        this.suggestion = suggestion;
    }

}

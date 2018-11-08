
package com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BymeGetRecommendedAffiliateName {

    @SerializedName("suggestions")
    @Expose
    private List<String> suggestions = new ArrayList<String>();
    @SerializedName("error")
    @Expose
    private GetUsernameSuggestionError getUsernameSuggestionError;

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public GetUsernameSuggestionError getGetUsernameSuggestionError() {
        return getUsernameSuggestionError;
    }

    public void setGetUsernameSuggestionError(GetUsernameSuggestionError getUsernameSuggestionError) {
        this.getUsernameSuggestionError = getUsernameSuggestionError;
    }

}

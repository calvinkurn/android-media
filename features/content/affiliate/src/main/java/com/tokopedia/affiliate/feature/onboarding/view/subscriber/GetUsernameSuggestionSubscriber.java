package com.tokopedia.affiliate.feature.onboarding.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion.GetUsernameSuggestionData;
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by milhamj on 10/4/18.
 */
public class GetUsernameSuggestionSubscriber extends Subscriber<GraphqlResponse> {
    private final UsernameInputContract.View view;

    public GetUsernameSuggestionSubscriber(UsernameInputContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.hideLoading();
        GetUsernameSuggestionData data = graphqlResponse.getData(GetUsernameSuggestionData.class);
        if (data != null && data.getSuggestion() != null
                && data.getSuggestion().getSuggestions() != null) {
            view.onSuccessGetUsernameSuggestion(
                    convertToLowerCase(data.getSuggestion().getSuggestions())
            );
        }
    }

    private List<String> convertToLowerCase(List<String> sourceStrings) {
        List<String> lowerCaseStrings = new ArrayList<>();
        for (int i = 0; i < sourceStrings.size(); i++) {
            lowerCaseStrings.add(sourceStrings.get(i).toLowerCase());
        }
        return lowerCaseStrings;
    }
}

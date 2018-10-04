package com.tokopedia.affiliate.feature.onboarding.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.affiliate.feature.onboarding.data.pojo.GetUsernameSuggestionData;
import com.tokopedia.affiliate.feature.onboarding.view.contract.UsernameInputContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

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
        view.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        GetUsernameSuggestionData data = graphqlResponse.getData(GetUsernameSuggestionData.class);
        if (data != null && data.getSuggestion() != null) {
            view.onSuccessGetUsernameSuggestion(data.getSuggestion().getSuggestions());
        }
    }
}

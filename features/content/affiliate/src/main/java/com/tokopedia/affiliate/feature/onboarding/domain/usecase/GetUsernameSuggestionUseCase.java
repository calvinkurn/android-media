package com.tokopedia.affiliate.feature.onboarding.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion.GetUsernameSuggestionData;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by milhamj on 10/4/18.
 */
public class GetUsernameSuggestionUseCase extends GraphqlUseCase {

    private final Context context;

    @Inject
    GetUsernameSuggestionUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Observable<GraphqlResponse> createObservable(RequestParams params) {
        String query = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_get_username_suggestion
        );

        this.clearRequest();
        this.addRequest(new GraphqlRequest(query, GetUsernameSuggestionData.class));
        return super.createObservable(params);
    }
}
package com.tokopedia.affiliate.feature.tracking.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.tracking.domain.model.Data;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class GetByMeUseCase {

    private static final String AFFILIATE_NAME = "name";
    private static final String URL_KEY = "key";
    private GraphqlUseCase graphqlUseCase;
    private Context context;

    @Inject
    GetByMeUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        GraphqlClient.init(context);
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void createObservable(String affiliateName, String urlKey,
                                 Subscriber<GraphqlResponse> subscriber) {
        Map<String, Object> variables = new HashMap<>();

        variables.put(AFFILIATE_NAME, affiliateName);
        variables.put(URL_KEY, urlKey);

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_url_tracking),
                Data.class,
                variables,
                false);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }
}

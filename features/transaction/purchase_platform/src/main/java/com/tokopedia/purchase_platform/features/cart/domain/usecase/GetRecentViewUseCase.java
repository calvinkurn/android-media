package com.tokopedia.purchase_platform.features.cart.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class GetRecentViewUseCase {

    private static final String USER_ID = "userID";

    private final Context context;
    private GraphqlUseCase graphqlUseCase;

    public GetRecentViewUseCase(Context context) {
        GraphqlClient.init(context);
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void createObservable(int userId, Subscriber<GraphqlResponse> graphqlResponseSubscriber) {

        graphqlUseCase.clearRequest();
        Map<String, Object> variables = new HashMap<>();
        variables.put(USER_ID, userId);

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.recent_view_query),
                GqlRecentViewResponse.class,
                variables, false);

        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(graphqlResponseSubscriber);

    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

}

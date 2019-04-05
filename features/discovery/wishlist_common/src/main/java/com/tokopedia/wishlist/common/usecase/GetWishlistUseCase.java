package com.tokopedia.wishlist.common.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.response.GetWishlistResponse;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class GetWishlistUseCase {

    private static final String PAGE = "page";
    private static final String COUNT = "count";
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_COUNT = 3;

    private final Context context;
    private GraphqlUseCase graphqlUseCase;

    public GetWishlistUseCase(Context context) {
        GraphqlClient.init(context);
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void createObservable(Subscriber<GraphqlResponse> graphqlResponseSubscriber) {

        graphqlUseCase.clearRequest();
        Map<String, Object> variables = new HashMap<>();

        variables.put(PAGE, DEFAULT_PAGE);
        variables.put(COUNT, DEFAULT_COUNT);

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_wishlist),
                GetWishlistResponse.class,
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

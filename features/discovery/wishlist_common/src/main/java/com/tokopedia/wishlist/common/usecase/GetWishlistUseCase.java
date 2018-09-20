package com.tokopedia.wishlist.common.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.response.GetWishlistResponse;
import com.tokopedia.wishlist.common.subscriber.AddWishListSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class GetWishlistUseCase {

    private static final String PAGE = "page";
    private static final String COUNT = "count";
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_COUNT = 2;

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
                variables);

        List<GraphqlRequest> graphqlRequestList = new ArrayList<>();
        graphqlRequestList.add(graphqlRequest);

        GraphqlCacheStrategy graphqlCacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build();

        Observable<GraphqlResponse> observable = ObservableFactory.create(graphqlRequestList,
                graphqlCacheStrategy);

        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.execute(graphqlResponseSubscriber);

    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

}

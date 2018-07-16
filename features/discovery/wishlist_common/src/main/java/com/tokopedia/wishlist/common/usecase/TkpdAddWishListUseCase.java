package com.tokopedia.wishlist.common.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.TkpdWishListActionListener;
import com.tokopedia.wishlist.common.response.TkpdAddWishListResponse;
import com.tokopedia.wishlist.common.subscriber.TkpdAddWishlistSubscriber;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class TkpdAddWishListUseCase {

    private final String PARAM_USER_ID = "userID";
    private final String PARAM_PRODUCT_ID = "productID";
    private final String OPERATION_NAME = "addWishlist";
    private final Context context;
    private GraphqlUseCase graphqlUseCase;

    public TkpdAddWishListUseCase(Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
    }

    public void createObservable(String productId, String userId, TkpdWishListActionListener wishlistActionListener) {

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_PRODUCT_ID, Integer.parseInt(productId));
        variables.put(PARAM_USER_ID, Integer.parseInt(userId));

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.query_add_wishlist),
                TkpdAddWishListResponse.class,
                variables, OPERATION_NAME);

        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.execute(new TkpdAddWishlistSubscriber(wishlistActionListener, context,productId));

    }

    public void createObservable(String productId, String userId, Subscriber subscriber) {

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_PRODUCT_ID, Integer.parseInt(productId));
        variables.put(PARAM_USER_ID, Integer.parseInt(userId));

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(), R.raw.query_add_wishlist),
                TkpdAddWishListResponse.class,
                variables, OPERATION_NAME);

        graphqlUseCase.addRequest(graphqlRequest);

        if (subscriber instanceof TkpdAddWishlistSubscriber)
            graphqlUseCase.execute(subscriber);

    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }
}

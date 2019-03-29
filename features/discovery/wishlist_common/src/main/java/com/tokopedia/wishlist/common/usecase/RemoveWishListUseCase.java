package com.tokopedia.wishlist.common.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.response.RemoveWishListResponse;
import com.tokopedia.wishlist.common.subscriber.RemoveWishListSubscriber;

import java.util.HashMap;
import java.util.Map;

public class RemoveWishListUseCase {

    private static final String PARAM_USER_ID = "userID";
    private static final String PARAM_PRODUCT_ID = "productID";
    private static final String OPERATION_NAME = "removeWishlist";
    private Context context;
    private GraphqlUseCase graphqlUseCase;

    public RemoveWishListUseCase(Context context) {
        graphqlUseCase = new GraphqlUseCase();
        this.context = context;
        GraphqlClient.init(context);
    }

    public void createObservable(String productId, String userId,
                                 WishListActionListener wishlistActionListener) {

        graphqlUseCase.clearRequest();

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_PRODUCT_ID, Integer.parseInt(productId));
        variables.put(PARAM_USER_ID, Integer.parseInt(userId));

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(),
                        R.raw.query_remove_wishlist),
                RemoveWishListResponse.class,
                variables, OPERATION_NAME, false);

        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.execute(new RemoveWishListSubscriber(wishlistActionListener, context, productId));

    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

}

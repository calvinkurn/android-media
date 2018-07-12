package com.tokopedia.wishlist.common.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.TkpdWishListActionListener;
import com.tokopedia.wishlist.common.response.TkpdRemoveWishListResponse;
import com.tokopedia.wishlist.common.subscriber.TkpdRemoveWishlistSubscriber;

import java.util.HashMap;
import java.util.Map;

public class TkpdRemoveWishListUseCase {

    public static final String PARAM_USER_ID = "userID";
    public static final String PARAM_PRODUCT_ID = "productID";
    private static final String OPERATION_NAME = "removeWishlist";
    private Context context;
    private GraphqlUseCase graphqlUseCase;

    public TkpdRemoveWishListUseCase(Context context) {
        this.context = context;
    }

    public void createObservable(String productId, String userId, TkpdWishListActionListener wishlistActionListener) {

        graphqlUseCase = new GraphqlUseCase();

        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_PRODUCT_ID, Integer.parseInt(productId));
        variables.put(PARAM_USER_ID, Integer.parseInt(userId));

        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_remove_wishlist),
                TkpdRemoveWishListResponse.class,
                variables, OPERATION_NAME);

        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.execute(new TkpdRemoveWishlistSubscriber(wishlistActionListener, productId));

    }

    public void unsubscribe() {
        graphqlUseCase.unsubscribe();
    }

}

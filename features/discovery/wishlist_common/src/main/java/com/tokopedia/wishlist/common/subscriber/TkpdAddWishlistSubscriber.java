package com.tokopedia.wishlist.common.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.TkpdWishListActionListener;
import com.tokopedia.wishlist.common.response.TkpdAddWishListResponse;

import rx.Subscriber;

public class TkpdAddWishlistSubscriber extends Subscriber<GraphqlResponse> {
    private final TkpdWishListActionListener viewListener;
    private String productId;

    public TkpdAddWishlistSubscriber(TkpdWishListActionListener viewListener, String productId) {
        this.viewListener = viewListener;
        this.productId = productId;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorAddWishList(
                ErrorHandler.getErrorMessage(e), productId);
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse != null) {
            TkpdAddWishListResponse addWishListResponse = graphqlResponse.getData(TkpdAddWishListResponse.class);
            if (addWishListResponse.getWishlist_add().getSuccess())
                viewListener.onSuccessAddWishlist(productId);
            else
                viewListener.onErrorAddWishList(
                        viewListener.getString(R.string.msg_error_add_wishlist),
                        productId);
        } else {
            viewListener.onErrorAddWishList(
                    viewListener.getString(R.string.default_request_error_unknown),
                    productId);
        }
    }
}
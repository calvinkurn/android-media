package com.tokopedia.wishlist.common.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.TkpdWishListActionListener;
import com.tokopedia.wishlist.common.response.TkpdRemoveWishListResponse;

import rx.Subscriber;

public class TkpdRemoveWishlistSubscriber extends Subscriber<GraphqlResponse> {

    private TkpdWishListActionListener viewListener;
    private String productId;

    public TkpdRemoveWishlistSubscriber(TkpdWishListActionListener viewListener,
                                          String productId) {

        this.viewListener = viewListener;
        this.productId = productId;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRemoveWishlist(
                ErrorHandler.getErrorMessage(e), productId);
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse != null) {
            TkpdRemoveWishListResponse removeWishListResponse = graphqlResponse.getData(TkpdRemoveWishListResponse.class);
            if (removeWishListResponse.getWishlistRemove().getSuccess()) {
                viewListener.onSuccessRemoveWishlist(productId);
            } else {
                viewListener.onErrorRemoveWishlist(
                        viewListener.getString(R.string.default_request_error_unknown),
                        productId);
            }

        } else {
            viewListener.onErrorRemoveWishlist(
                    viewListener.getString(R.string.default_request_error_unknown),
                    productId);
        }

    }
}

package com.tokopedia.wishlist.common.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.TkpdWishListActionListener;
import com.tokopedia.wishlist.common.response.TkpdRemoveWishListResponse;

import rx.Subscriber;

public class TkpdRemoveWishlistSubscriber extends Subscriber<GraphqlResponse> {

    private TkpdWishListActionListener viewListener;
    private String productId;
    private Context context;

    public TkpdRemoveWishlistSubscriber(TkpdWishListActionListener viewListener,
                                        Context context,
                                        String productId) {

        this.viewListener = viewListener;
        this.productId = productId;
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRemoveWishlist(
                ErrorHandler.getErrorMessage(context, e), productId);
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

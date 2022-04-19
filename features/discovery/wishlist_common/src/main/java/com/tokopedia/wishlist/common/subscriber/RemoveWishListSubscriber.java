package com.tokopedia.wishlist.common.subscriber;

import android.content.Context;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.response.RemoveWishListResponse;

import rx.Subscriber;

public class RemoveWishListSubscriber extends Subscriber<GraphqlResponse> {

    private WishListActionListener viewListener;
    private String productId;
    private Context context;

    public RemoveWishListSubscriber(WishListActionListener viewListener,
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
            RemoveWishListResponse removeWishListResponse = graphqlResponse.getData(RemoveWishListResponse.class);
            if (removeWishListResponse.getWishlistRemove().getSuccess()) {
                viewListener.onSuccessRemoveWishlist(productId);
            } else {
                viewListener.onErrorRemoveWishlist(
                        context.getString(R.string.default_request_error_unknown),
                        productId);
            }

        } else {
            viewListener.onErrorRemoveWishlist(
                    context.getString(R.string.default_request_error_unknown),
                    productId);
        }

    }
}

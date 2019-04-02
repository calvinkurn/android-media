package com.tokopedia.wishlist.common.subscriber;

import android.content.Context;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.wishlist.common.R;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.response.AddWishListResponse;

import rx.Subscriber;

public class AddWishListSubscriber extends Subscriber<GraphqlResponse> {
    private final WishListActionListener viewListener;
    private String productId;
    private Context context;

    public AddWishListSubscriber(WishListActionListener viewListener, Context context,
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
        viewListener.onErrorAddWishList(
                ErrorHandler.getErrorMessage(context, e), productId);
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse != null) {
            AddWishListResponse addWishListResponse = graphqlResponse
                    .getData(AddWishListResponse.class);
            if (addWishListResponse.getWishlist_add().getSuccess())
                viewListener.onSuccessAddWishlist(productId);
            else
                viewListener.onErrorAddWishList(
                        context.getString(R.string.msg_error_add_wishlist),
                        productId);
        } else {
            viewListener.onErrorAddWishList(
                    context.getString(R.string.default_request_error_unknown),
                    productId);
        }
    }
}
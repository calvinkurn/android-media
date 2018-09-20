package com.tokopedia.checkout.view.feature.emptycart.subscriber;

import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.wishlist.common.response.GetWishlistResponse;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class GetWishlistSubscriber extends Subscriber<GraphqlResponse> {

    private EmptyCartContract.View view;

    public GetWishlistSubscriber(EmptyCartContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            view.renderHasNoWishlist();
        }
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        if (view != null) {
            if (graphqlResponse != null && graphqlResponse.getData(GetWishlistResponse.class) != null) {
                GetWishlistResponse getWishlistResponse = graphqlResponse.getData(GetWishlistResponse.class);
                if (getWishlistResponse != null && getWishlistResponse.getGqlWishList() != null) {
                    view.renderHasWishlist(getWishlistResponse.getGqlWishList().getWishlistDataList());
                } else {
                    view.renderHasNoWishlist();
                }
            } else {
                view.renderHasNoWishlist();
            }
        }
    }

}

package com.tokopedia.checkout.view.feature.emptycart.subscriber;

import com.tokopedia.checkout.view.feature.emptycart.EmptyCartApi;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.wishlist.common.response.GetWishlistResponse;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class GetWishlistSubscriber extends Subscriber<GraphqlResponse> {

    private static final int ITEM_SHOW_COUNT = 2;
    private EmptyCartContract.View view;
    private EmptyCartContract.Presenter presenter;

    public GetWishlistSubscriber(EmptyCartContract.View view, EmptyCartContract.Presenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            view.renderHasNoWishList();
            stopTrace();
        }
    }

    private void stopTrace() {
        if (!view.isAllTraceStopped()) {
            presenter.setLoadApiStatus(EmptyCartApi.WISH_LIST, true);
            view.stopAllTrace();
        }
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        if (view != null) {
            if (graphqlResponse != null && graphqlResponse.getData(GetWishlistResponse.class) != null) {
                GetWishlistResponse getWishlistResponse = graphqlResponse.getData(GetWishlistResponse.class);
                if (getWishlistResponse != null && getWishlistResponse.getGqlWishList() != null &&
                        getWishlistResponse.getGqlWishList().getWishlistDataList() != null &&
                        getWishlistResponse.getGqlWishList().getWishlistDataList().size() > 0) {
                    presenter.setWishListViewModels(getWishlistResponse.getGqlWishList().getWishlistDataList());
                    view.renderHasWishList(getWishlistResponse.getGqlWishList().getWishlistDataList().size() > ITEM_SHOW_COUNT);
                } else {
                    view.renderHasNoWishList();
                }
            } else {
                view.renderHasNoWishList();
            }

            stopTrace();
        }
    }

}

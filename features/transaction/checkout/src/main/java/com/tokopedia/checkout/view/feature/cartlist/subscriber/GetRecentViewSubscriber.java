package com.tokopedia.checkout.view.feature.cartlist.subscriber;

import com.tokopedia.checkout.domain.datamodel.recentview.GqlRecentViewResponse;
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter;
import com.tokopedia.checkout.view.feature.cartlist.ICartListView;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartApi;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class GetRecentViewSubscriber extends Subscriber<GraphqlResponse> {

    private static final int ITEM_SHOW_COUNT = 2;
    private final ICartListView view;
    private final ICartListPresenter presenter;

    public GetRecentViewSubscriber(ICartListView view, ICartListPresenter presenter) {
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
//            view.renderHasNoRecentView();
            stopTrace();
        }
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        if (view != null) {
            if (graphqlResponse != null && graphqlResponse.getData(GqlRecentViewResponse.class) != null) {
                GqlRecentViewResponse gqlRecentViewResponse = graphqlResponse.getData(GqlRecentViewResponse.class);
                if (gqlRecentViewResponse != null && gqlRecentViewResponse.getGqlRecentView() != null &&
                        gqlRecentViewResponse.getGqlRecentView().getRecentViewList() != null &&
                        gqlRecentViewResponse.getGqlRecentView().getRecentViewList().size() > 0) {
                    view.renderRecentView(gqlRecentViewResponse.getGqlRecentView().getRecentViewList());
//                    presenter.setRecentViewListModels(gqlRecentViewResponse.getGqlRecentView().getRecentViewList());
//                    view.renderHasRecentView(gqlRecentViewResponse.getGqlRecentView().getRecentViewList().size() > ITEM_SHOW_COUNT);
//                } else {
//                    view.renderHasNoRecentView();
                }
//            } else {
//                view.renderHasNoRecentView();
            }
            stopTrace();
        }
    }

    private void stopTrace() {
//        if (!view.isAllTraceStopped()) {
//            presenter.setLoadApiStatus(EmptyCartApi.LAST_SEEN, true);
//            view.stopAllTrace();
//        }
    }

}

package com.tokopedia.affiliate.feature.dashboard.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * @author by yfsx on 19/09/18.
 */
public class GetDashboardLoadMoreSubscriber extends Subscriber<GraphqlResponse> {

    private DashboardContract.View mainView;


    public GetDashboardLoadMoreSubscriber(DashboardContract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.hideLoading();
        mainView.onErrorGetDashboardItem(ErrorHandler.getErrorMessage(mainView.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        DashboardQuery query = response.getData(DashboardQuery.class);
        mainView.onSuccessLoadMoreDashboardItem(
                query.getProduct().getAffiliatedProducts() != null?
                        GetDashboardSubscriber.mappingListItem(query.getProduct().getAffiliatedProducts())
                        : new ArrayList<>(),
                query.getProduct().getPagination().getNextCursor());
    }
}

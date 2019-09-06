package com.tokopedia.affiliate.feature.dashboard.view.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateCuratedProductContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * @author by yfsx on 19/09/18.
 */
public class GetDashboardLoadMoreSubscriber extends Subscriber<GraphqlResponse> {

    private AffiliateCuratedProductContract.View mainView;
    private Integer type;

    public GetDashboardLoadMoreSubscriber(Integer type, AffiliateCuratedProductContract.View mainView) {
        this.mainView = mainView;
        this.type = type;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.hideLoading();
        mainView.onErrorGetDashboardItem(ErrorHandler.getErrorMessage(mainView.getCtx(), e));
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        DashboardQuery query = response.getData(DashboardQuery.class);
        Context context = mainView.getCtx();
        mainView.onSuccessLoadMoreDashboardItem(
                query.getProduct().getAffiliatedProducts() != null && context != null ?
                        GetDashboardSubscriber.mappingListItem(context, type, query.getProduct().getAffiliatedProducts(), query.getProduct().getSubtitles())
                        : new ArrayList<>(),
                query.getProduct().getPagination().getNextCursor());
    }
}

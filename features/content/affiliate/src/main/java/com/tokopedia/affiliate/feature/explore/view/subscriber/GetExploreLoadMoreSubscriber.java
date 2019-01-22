package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreData;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by yfsx on 08/10/18.
 */
public class GetExploreLoadMoreSubscriber extends Subscriber<GraphqlResponse> {

    private ExploreContract.View mainView;

    public GetExploreLoadMoreSubscriber(ExploreContract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        if (mainView == null)
            return;
        mainView.hideLoading();
        mainView.onErrorGetMoreData(ErrorHandler.getErrorMessage(mainView.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        ExploreData query = response.getData(ExploreData.class);

        mainView.onSuccessGetMoreData(
                GetExploreFirstSubscriber.mappingProducts(query.getExploreProduct().getProducts(), mainView),
                query.getExploreProduct().getPagination().getNextCursor()
        );
    }
}

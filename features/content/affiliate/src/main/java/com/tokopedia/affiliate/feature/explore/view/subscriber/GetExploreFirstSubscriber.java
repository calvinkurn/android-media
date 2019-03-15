package com.tokopedia.affiliate.feature.explore.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreData;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by yfsx on 08/10/18.
 */
public class GetExploreFirstSubscriber extends Subscriber<GraphqlResponse> {

    protected ExploreContract.View mainView;
    protected boolean isPullToRefresh;
    private ExploreParams exploreParams;
    private boolean isSearch;

    public GetExploreFirstSubscriber(ExploreContract.View mainView,
                                     boolean isSearch,
                                     boolean isPullToRefresh,
                                     ExploreParams exploreParams) {
        this.mainView = mainView;
        this.isSearch = isSearch;
        this.isPullToRefresh = isPullToRefresh;
        this.exploreParams = exploreParams;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        if (mainView == null) {
            return;
        }
        mainView.hideLoading();
        mainView.onErrorGetFirstData(ErrorHandler.getErrorMessage(mainView.getContext(), e));
        mainView.stopTrace();
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        ExploreData query = response.getData(ExploreData.class);
        if (isSearch && query.getExploreProduct() != null
                && query.getExploreProduct().getProducts() == null) {
            mainView.getAffiliateAnalytics().onSearchNotFound(exploreParams.getKeyword());
            mainView.onEmptySearchResult();
        } else {
            //TODO milhamj
//            ExploreProduct exploreQuery = query.getExploreProduct();
//            if (isFirstDataWithFilterSort(exploreParams)) {
//                mainView.onSuccessGetFilteredSortedFirstData(
//                        exploreQuery.getProducts() != null ?
//                                mappingProducts(exploreQuery.getProducts(), mainView) :
//                                new ArrayList<>(),
//                        exploreQuery.getPagination() != null ?
//                                exploreQuery.getPagination().getNextCursor() :
//                                "",
//                        isSearch,
//                        isPullToRefresh);
//            } else {
//                mainView.onSuccessGetFirstData(
//                        exploreQuery.getProducts() != null ?
//                                mappingProducts(exploreQuery.getProducts(), mainView) :
//                                new ArrayList<>(),
//                        exploreQuery.getPagination() != null ?
//                                exploreQuery.getPagination().getNextCursor() :
//                                "",
//                        isSearch,
//                        isPullToRefresh,
//                        mappingSortFilter(query.getFilter(), query.getSort())
//                );
//            }
        }

        mainView.stopTrace();
    }

    private boolean isFirstDataWithFilterSort(ExploreParams exploreParams) {
        return exploreParams.getFilters().size() != 0 || !TextUtils.isEmpty(exploreParams.getSort().getText());
    }
}

package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.analytics.AffiliateEventTracking;
import com.tokopedia.affiliate.feature.explore.data.pojo.CategoryPojo;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreData;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreProductPojo;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreQuery;
import com.tokopedia.affiliate.feature.explore.data.pojo.FilterQuery;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortFilterModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by yfsx on 08/10/18.
 */
public class GetExploreFirstSubscriber extends Subscriber<GraphqlResponse> {

    protected ExploreContract.View mainView;
    protected boolean isSearch;
    protected boolean isPullToRefresh;
    protected ExploreParams exploreParams;

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
        if (mainView == null)
            return;
        mainView.hideLoading();
        mainView.onErrorGetFirstData(ErrorHandler.getErrorMessage(mainView.getContext(), e));
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
            ExploreQuery exploreQuery = query.getExploreProduct();
            if (isFirstDataWithFilterSort(exploreParams)) {
                mainView.onSuccessGetFilteredSortedFirstData(
                        exploreQuery.getProducts() != null ?
                                mappingProducts(exploreQuery.getProducts(), mainView) :
                                new ArrayList<>(),
                        exploreQuery.getPagination() != null ?
                                exploreQuery.getPagination().getNextCursor() :
                                "",
                        isSearch,
                        isPullToRefresh);
            } else {
                mainView.onSuccessGetFirstData(
                        exploreQuery.getProducts() != null ?
                                mappingProducts(exploreQuery.getProducts(), mainView) :
                                new ArrayList<>(),
                        exploreQuery.getPagination() != null ?
                                exploreQuery.getPagination().getNextCursor() :
                                "",
                        isSearch,
                        isPullToRefresh,
                        mappingSortFilter(query.getFilter())
                );
            }
        }
    }

    private SortFilterModel mappingSortFilter(FilterQuery filterPojo) {
        return new SortFilterModel(mappingFilterModel(filterPojo.getCategory()));
    }

    private List<FilterViewModel> mappingFilterModel(List<CategoryPojo> pojoList) {
        List<FilterViewModel> itemList = new ArrayList<>();
        for (CategoryPojo pojo : pojoList) {
            FilterViewModel item = new FilterViewModel(pojo.getName(), pojo.getImage(), pojo.getIds(), false);
            itemList.add(item);
        }
        return itemList;

    }

    public static List<Visitable> mappingProducts(List<ExploreProductPojo> pojoList, ExploreContract.View mainView) {
        List<Visitable> itemList = new ArrayList<>();
        for (ExploreProductPojo pojo : pojoList) {
            itemList.add(
                    new ExploreViewModel(
                            pojo.getAdId(),
                            pojo.getImage(),
                            pojo.getName(),
                            pojo.getCommission(),
                            pojo.getProductId(),
                            ""));
            mainView.getAffiliateAnalytics().onProductImpression(pojo.getProductId());
        }
        return itemList;
    }

    private boolean isFirstDataWithFilterSort(ExploreParams exploreParams) {
        return exploreParams.getFilters().size()!=0;
    }
}

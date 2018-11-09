package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreData;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreProductPojo;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreQuery;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by yfsx on 08/10/18.
 */
public class GetExploreFirstSubscriber extends Subscriber<GraphqlResponse> {

    private ExploreContract.View mainView;
    private boolean isSearch;

    public GetExploreFirstSubscriber(ExploreContract.View mainView, boolean isSearch) {
        this.mainView = mainView;
        this.isSearch = isSearch;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        mainView.hideLoading();
        mainView.onErrorGetFirstData(ErrorHandler.getErrorMessage(mainView.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        ExploreData query = response.getData(ExploreData.class);
        if (isSearch && query.getExploreProduct() != null
                && query.getExploreProduct().getProducts() == null) {
            mainView.onEmptySearchResult();
        } else {
            ExploreQuery exploreQuery = query.getExploreProduct();
            mainView.onSuccessGetFirstData(
                    exploreQuery.getProducts() != null ?
                            mappingProducts(exploreQuery.getProducts()) :
                            new ArrayList<>(),
                    exploreQuery.getPagination() != null ?
                            exploreQuery.getPagination().getNextCursor() :
                            ""
            );
        }
    }

    public static List<Visitable> mappingProducts(List<ExploreProductPojo> pojoList) {
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
        }
        return itemList;
    }
}

package com.tokopedia.affiliate.feature.explore.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
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

    public GetExploreFirstSubscriber(ExploreContract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.hideLoading();
        mainView.onErrorGetFirstData(ErrorHandler.getErrorMessage(mainView.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        ExploreQuery query = response.getData(ExploreQuery.class);
        mainView.onSuccessGetFirstData(
                query.getProducts() != null ?
                        mappingProducts(query.getProducts()) :
                        new ArrayList<Visitable>(),
                query.getPagination() != null ?
                        query.getPagination().getNextCursor() :
                        ""
        );    }

    public static List<Visitable> mappingProducts(List<ExploreProductPojo> pojoList) {
        List<Visitable> itemList = new ArrayList<>();
        for (ExploreProductPojo pojo : pojoList) {
            itemList.add(new ExploreViewModel(pojo.getAdId(), pojo.getImage(), pojo.getName(), pojo.getCommission(), pojo.getProductId(), ""));
        }
        return itemList;
    }
}

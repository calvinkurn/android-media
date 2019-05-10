package com.tokopedia.affiliate.feature.dashboard.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardHeaderPojo;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardItemPojo;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuotaStatus;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardFloatingButtonViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by yfsx on 19/09/18.
 */
public class GetDashboardSubscriber extends Subscriber<GraphqlResponse> {

    private DashboardContract.View mainView;

    public GetDashboardSubscriber(DashboardContract.View mainView) {
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
        mainView.onSuccessGetDashboardItem(
                mappingHeader(query.getAffiliateStats()),
                query.getProduct().getAffiliatedProducts() != null ? mappingListItem(query.getProduct().getAffiliatedProducts()) : new ArrayList<>(),
                query.getProduct().getPagination().getNextCursor(),
                mappingFloatingItem(query.getPostQuota())
        );
    }

    private DashboardHeaderViewModel mappingHeader(DashboardHeaderPojo pojo) {
        return new DashboardHeaderViewModel(
                pojo.getTotalCommission(),
                pojo.getProfileView(),
                pojo.getProductClick(),
                pojo.getProductSold());
    }

    public static List<DashboardItemViewModel> mappingListItem(List<DashboardItemPojo> pojoList) {
        List<DashboardItemViewModel> itemList = new ArrayList<>();
        for (DashboardItemPojo pojo : pojoList) {
            DashboardItemViewModel item = new DashboardItemViewModel(
                    pojo.getId(),
                    pojo.getImage(),
                    pojo.getName(),
                    pojo.getCommission(),
                    pojo.getTotalClick(),
                    pojo.getTotalSold(),
                    pojo.getProductCommission() != null ? pojo.getProductCommission() : "",
                    pojo.isIsActive());
            itemList.add(item);
        }
        return itemList;
    }

    public static DashboardFloatingButtonViewModel mappingFloatingItem(DashboardQuotaStatus pojo) {
        return new DashboardFloatingButtonViewModel(pojo.getFormatted(), pojo.getNumber());
    }
}

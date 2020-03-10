package com.tokopedia.sellerapp.dashboard.usecase;

import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCaseRx;
import com.tokopedia.shop.common.domain.interactor.GetShopRatingUseCaseRx;
import com.tokopedia.shop.common.domain.interactor.GetShopReputationUseCaseRx;
import com.tokopedia.shop.common.domain.interactor.GetShopSatisfactionUseCaseRx;
import com.tokopedia.shop.common.domain.interactor.GetShopTxStatsUseCase;
import com.tokopedia.sellerapp.dashboard.model.ShopInfoDashboardModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public class GetShopInfoDashboardDataUseCase extends UseCase<ShopInfoDashboardModel> {

    private static final String PARAM_SHOP_ID = "shopId";
    private GetShopInfoUseCaseRx getShopInfoUseCaseRx;
    private GetShopTxStatsUseCase getShopTxStatsUseCase;
    private GetShopReputationUseCaseRx getShopReputationUseCaseRx;
    private GetShopRatingUseCaseRx getShopRatingUseCaseRx;
    private GetShopSatisfactionUseCaseRx getShopSatisfactionUseCaseRx;

    private RequestParams params = RequestParams.EMPTY;

    @Inject
    public GetShopInfoDashboardDataUseCase(
            GetShopInfoUseCaseRx getShopInfoUseCaseRx,
            GetShopTxStatsUseCase getShopTxStatsUseCase,
            GetShopReputationUseCaseRx getShopReputationUseCaseRx,
            GetShopRatingUseCaseRx getShopRatingUseCaseRx,
            GetShopSatisfactionUseCaseRx getShopSatisfactionUseCaseRx
    ) {
        this.getShopInfoUseCaseRx = getShopInfoUseCaseRx;
        this.getShopTxStatsUseCase = getShopTxStatsUseCase;
        this.getShopReputationUseCaseRx = getShopReputationUseCaseRx;
        this.getShopRatingUseCaseRx = getShopRatingUseCaseRx;
        this.getShopSatisfactionUseCaseRx = getShopSatisfactionUseCaseRx;
    }

    static RequestParams createParams(int shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_SHOP_ID, shopId);
        return requestParams;
    }

    RequestParams getParams() {
        return params;
    }

    void setParams(RequestParams params) {
        this.params = params;
    }

    @Override
    public Observable<ShopInfoDashboardModel> createObservable(RequestParams requestParams) {
        int shopId = requestParams.getInt(PARAM_SHOP_ID, 0);
        ArrayList<Integer> listShopId = new ArrayList<>();
        listShopId.add(shopId);
        getShopInfoUseCaseRx.setParams(GetShopInfoUseCaseRx.createParams(listShopId));
        getShopTxStatsUseCase.setParams(GetShopTxStatsUseCase.createParams(shopId));
        getShopReputationUseCaseRx.setParams(GetShopReputationUseCaseRx.createParams(listShopId));
        getShopRatingUseCaseRx.setParams(GetShopRatingUseCaseRx.createParams(shopId));
        getShopSatisfactionUseCaseRx.setParams(GetShopSatisfactionUseCaseRx.createParams(shopId));
        return Observable.zip(
                getShopInfoUseCaseRx.createObservable(getShopInfoUseCaseRx.getParams()).subscribeOn(Schedulers.io()),
                getShopTxStatsUseCase.createObservable(getShopTxStatsUseCase.getParams()).subscribeOn(Schedulers.io()),
                getShopReputationUseCaseRx.createObservable(getShopReputationUseCaseRx.getParams()).subscribeOn(Schedulers.io()),
                getShopRatingUseCaseRx.createObservable(getShopRatingUseCaseRx.getParams()).subscribeOn(Schedulers.io()),
                getShopSatisfactionUseCaseRx.createObservable(getShopSatisfactionUseCaseRx.getParams()).subscribeOn(Schedulers.io()),
                (ShopInfoDashboardModel::new)
        );
    }
}

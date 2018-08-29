package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositUseCase;
import com.tokopedia.topads.dashboard.data.model.data.DashboardPopulateResponse;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by hadi.putra on 18/05/18.
 */

public class TopAdsGetPopulateDataAdUseCase extends UseCase<DashboardPopulateResponse> {
    private TopAdsPopulateTotalAdsUseCase topAdsPopulateTotalAdsUseCase;
    private TopAdsGetShopDepositUseCase topAdsGetShopDepositUseCase;

    @Inject
    public TopAdsGetPopulateDataAdUseCase(TopAdsPopulateTotalAdsUseCase topAdsPopulateTotalAdsUseCase,
                                          TopAdsGetShopDepositUseCase topAdsGetShopDepositUseCase) {
        this.topAdsPopulateTotalAdsUseCase = topAdsPopulateTotalAdsUseCase;
        this.topAdsGetShopDepositUseCase = topAdsGetShopDepositUseCase;
    }

    @Override
    public Observable<DashboardPopulateResponse> createObservable(RequestParams requestParams) {

        Observable<TotalAd> totalAdObservable = topAdsPopulateTotalAdsUseCase.createObservable(requestParams);
        Observable<DataDeposit> depositObservable = topAdsGetShopDepositUseCase.createObservable(requestParams);

        return Observable.zip(totalAdObservable, depositObservable, new Func2<TotalAd, DataDeposit, DashboardPopulateResponse>() {
            @Override
            public DashboardPopulateResponse call(TotalAd totalAd, DataDeposit dataDeposit) {
                return new DashboardPopulateResponse(totalAd, dataDeposit);
            }
        });
    }

    public static RequestParams createRequestParams(String shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId);
        return requestParams;
    }
}

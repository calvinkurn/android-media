package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase;
import com.tokopedia.topads.dashboard.data.model.DashboardPopulateResponse;
import com.tokopedia.topads.dashboard.data.model.TotalAd;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by hadi.putra on 18/05/18.
 */

public class TopAdsGetPopulateDataAdUseCase extends UseCase<DashboardPopulateResponse> {
    private static final String PARAM_QUERY = "query";

    private TopAdsPopulateTotalAdsUseCase topAdsPopulateTotalAdsUseCase;
    private TopAdsGetShopDepositGraphQLUseCase topAdsGetShopDepositUseCase;

    @Inject
    public TopAdsGetPopulateDataAdUseCase(TopAdsPopulateTotalAdsUseCase topAdsPopulateTotalAdsUseCase,
                                          TopAdsGetShopDepositGraphQLUseCase topAdsGetShopDepositUseCase) {
        this.topAdsPopulateTotalAdsUseCase = topAdsPopulateTotalAdsUseCase;
        this.topAdsGetShopDepositUseCase = topAdsGetShopDepositUseCase;
    }

    @Override
    public Observable<DashboardPopulateResponse> createObservable(RequestParams requestParams) {
        Observable<DataDeposit> depositObservable = topAdsGetShopDepositUseCase.createObservable(requestParams);
        RequestParams totalAdsRequestParams = createTotalAdsRequestParams(requestParams.getInt(TopAdsCommonConstant.PARAM_SHOP_ID, 0));
        Observable<TotalAd> totalAdObservable = topAdsPopulateTotalAdsUseCase.createObservable(totalAdsRequestParams);

        return Observable.zip(totalAdObservable, depositObservable, new Func2<TotalAd, DataDeposit, DashboardPopulateResponse>() {
            @Override
            public DashboardPopulateResponse call(TotalAd totalAd, DataDeposit dataDeposit) {
                return new DashboardPopulateResponse(totalAd, dataDeposit);
            }
        });
    }

    public static RequestParams createRequestParams(String queryDeposit, int shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_QUERY, queryDeposit);
        requestParams.putInt(TopAdsCommonConstant.PARAM_SHOP_ID, shopId);
        return requestParams;
    }

    private RequestParams createTotalAdsRequestParams(int shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, String.valueOf(shopId));
        return requestParams;
    }
}

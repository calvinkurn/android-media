package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.domain.GetDepositTopAdsRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

public class GetDepositTopAdsUseCase extends UseCase<DataDeposit> {

    private GetDepositTopAdsRepository getDepositTopAdsRepository;

    public GetDepositTopAdsUseCase(GetDepositTopAdsRepository getDepositTopAdsRepository) {
        super();
        this.getDepositTopAdsRepository = getDepositTopAdsRepository;
    }

    @Override
    public Observable<DataDeposit> createObservable(RequestParams requestParams) {
        return getDepositTopAdsRepository.getDeposit(requestParams.getParamsAllValueInString());
    }

    public static RequestParams createRequestParams(String shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        return requestParams;
    }
}

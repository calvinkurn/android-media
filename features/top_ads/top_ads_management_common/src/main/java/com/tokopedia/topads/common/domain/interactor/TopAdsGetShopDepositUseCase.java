package com.tokopedia.topads.common.domain.interactor;

import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.domain.repository.TopAdsShopDepositRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class TopAdsGetShopDepositUseCase extends UseCase<DataDeposit> {

    private final TopAdsShopDepositRepository topAdsShopDepositRepository;

    @Inject
    public TopAdsGetShopDepositUseCase(TopAdsShopDepositRepository topAdsShopDepositRepository) {
        this.topAdsShopDepositRepository = topAdsShopDepositRepository;
    }

    @Override
    public Observable<DataDeposit> createObservable(RequestParams requestParams) {
        return topAdsShopDepositRepository.getDeposit(requestParams);
    }

    public static RequestParams createParams(String shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId);
        return requestParams;
    }
}

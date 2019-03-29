package com.tokopedia.topads.common.domain.interactor;

import com.tokopedia.topads.common.data.model.DataCheckPromo;
import com.tokopedia.topads.common.domain.repository.TopAdsCheckProductPromoRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 12/04/18.
 */

public class TopAdsCheckProductPromoUseCase extends UseCase<DataCheckPromo> {
    private static final String PARAM_ITEM_ID = "item_id";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_SHOP_ID = "shop_id";

    private final TopAdsCheckProductPromoRepository topAdsCheckProductPromoRepository;

    @Inject
    public TopAdsCheckProductPromoUseCase(TopAdsCheckProductPromoRepository topAdsCheckProductPromoRepository) {
        this.topAdsCheckProductPromoRepository = topAdsCheckProductPromoRepository;
    }

    @Override
    public Observable<DataCheckPromo> createObservable(RequestParams requestParams) {
        return topAdsCheckProductPromoRepository.getProductPromoTopAds(requestParams);
    }

    public static RequestParams createRequestParams(String shopId, String itemId, String userId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_SHOP_ID, shopId);
        requestParams.putString(PARAM_ITEM_ID, itemId);
        requestParams.putString(PARAM_USER_ID, userId);
        return requestParams;
    }
}

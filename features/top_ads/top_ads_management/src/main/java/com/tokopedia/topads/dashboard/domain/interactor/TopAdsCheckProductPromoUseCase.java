package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.domain.TopAdsCheckProductPromoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 12/04/18.
 */

public class TopAdsCheckProductPromoUseCase extends UseCase<String> {
    private static final String PARAM_ITEM_ID = "item_id";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_SHOP_ID = "shop_id";

    private final TopAdsCheckProductPromoRepository topAdsCheckProductPromoRepository;

    @Inject
    public TopAdsCheckProductPromoUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                          TopAdsCheckProductPromoRepository topAdsCheckProductPromoRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsCheckProductPromoRepository = topAdsCheckProductPromoRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
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

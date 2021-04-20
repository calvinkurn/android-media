package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail;


import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 9/26/17.
 */

public class CheckShopFavoritedUseCase extends UseCase<CheckShopFavoriteDomain> {

    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_SHOP_ID = "PARAM_SHOP_ID";
    private ReputationRepository reputationRepository;

    public CheckShopFavoritedUseCase(ReputationRepository reputationRepository) {
        super();
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<CheckShopFavoriteDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.checkIsShopFavorited(requestParams);
    }

    public static RequestParams getParam(String loginID, int shopId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, loginID);
        params.putString(PARAM_SHOP_ID, String.valueOf(shopId));
        return params;
    }

}

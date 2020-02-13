package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 9/26/17.
 */

public class FavoriteShopUseCase extends UseCase<FavoriteShopDomain> {

    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_SOURCE = "src";

    private ReputationRepository reputationRepository;

    public FavoriteShopUseCase(ReputationRepository reputationRepository) {
        super();
        this.reputationRepository = reputationRepository;
    }

    public static RequestParams getParam(int shopId, String src) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_SHOP_ID, String.valueOf(shopId));
        params.putString(PARAM_SOURCE, src);
        return params;
    }

    @Override
    public Observable<FavoriteShopDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.favoriteShop(requestParams);
    }
}

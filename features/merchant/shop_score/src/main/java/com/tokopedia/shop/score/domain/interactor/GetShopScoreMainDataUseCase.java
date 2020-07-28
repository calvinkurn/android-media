package com.tokopedia.shop.score.domain.interactor;


import com.tokopedia.shop.score.domain.ShopScoreRepository;
import com.tokopedia.shop.score.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class GetShopScoreMainDataUseCase extends UseCase<ShopScoreMainDomainModel> {
    private final ShopScoreRepository shopScoreRepository;

    @Inject
    public GetShopScoreMainDataUseCase(ShopScoreRepository shopScoreRepository) {
        super();
        this.shopScoreRepository = shopScoreRepository;
    }

    @Override
    public Observable<ShopScoreMainDomainModel> createObservable(RequestParams requestParams) {
        return shopScoreRepository.getShopScoreSummary();
    }
}

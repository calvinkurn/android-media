package com.tokopedia.shop.score.domain.interactor;

import com.tokopedia.shop.score.domain.ShopScoreRepository;
import com.tokopedia.shop.score.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class GetShopScoreDetailUseCase extends UseCase<ShopScoreDetailDomainModel> {
    private final ShopScoreRepository shopScoreRepository;

    public GetShopScoreDetailUseCase(ShopScoreRepository shopScoreRepository) {
        super();
        this.shopScoreRepository = shopScoreRepository;
    }

    @Override
    public Observable<ShopScoreDetailDomainModel> createObservable(RequestParams requestParams) {
        return shopScoreRepository.getShopScoreDetail();
    }
}

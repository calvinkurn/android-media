package com.tokopedia.shop_score.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
//import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.shop_score.domain.ShopScoreRepository;
import com.tokopedia.shop_score.domain.model.ShopScoreDetailDomainModel;
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
    public Observable<ShopScoreDetailDomainModel> createObservable(com.tokopedia.usecase.RequestParams requestParams) {
        return shopScoreRepository.getShopScoreDetail();
    }
}

package com.tokopedia.shop.score.domain;

import com.tokopedia.shop.score.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.shop.score.domain.model.ShopScoreMainDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public interface ShopScoreRepository {
    Observable<ShopScoreMainDomainModel> getShopScoreSummary();

    Observable<ShopScoreDetailDomainModel> getShopScoreDetail();
}

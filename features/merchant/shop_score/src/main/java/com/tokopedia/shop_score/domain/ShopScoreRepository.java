package com.tokopedia.shop_score.domain;

import com.tokopedia.shop_score.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.shop_score.domain.model.ShopScoreMainDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public interface ShopScoreRepository {
    Observable<ShopScoreMainDomainModel> getShopScoreSummary();

    Observable<ShopScoreDetailDomainModel> getShopScoreDetail();
}

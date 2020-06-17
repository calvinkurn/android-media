package com.tokopedia.shop_score.data.repository;

import com.tokopedia.shop_score.data.factory.ShopScoreFactory;
import com.tokopedia.shop_score.data.source.ShopScoreDetailDataSource;
import com.tokopedia.shop_score.data.source.ShopScoreSummaryDataSource;
import com.tokopedia.shop_score.domain.ShopScoreRepository;
import com.tokopedia.shop_score.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.shop_score.domain.model.ShopScoreMainDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */

public class ShopScoreRepositoryImpl implements ShopScoreRepository {
    private final ShopScoreFactory shopScoreFactory;

    public ShopScoreRepositoryImpl(ShopScoreFactory shopScoreFactory) {
        this.shopScoreFactory = shopScoreFactory;
    }

    @Override
    public Observable<ShopScoreMainDomainModel> getShopScoreSummary() {
        ShopScoreSummaryDataSource shopScoreSummaryDataSource = shopScoreFactory.createShopScoreSummarySource();
        return shopScoreSummaryDataSource.getShopScoreSummary();
    }

    @Override
    public Observable<ShopScoreDetailDomainModel> getShopScoreDetail() {
        ShopScoreDetailDataSource shopScoreDetailDataSource = shopScoreFactory.createShopScoreDetailSource();
        return shopScoreDetailDataSource.getShopScoreDetail();
    }
}

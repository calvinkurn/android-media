package com.tokopedia.shop.score.data.factory;

import com.tokopedia.shop.score.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.shop.score.data.source.ShopScoreDetailDataSource;
import com.tokopedia.shop.score.data.source.ShopScoreSummaryDataSource;
import com.tokopedia.shop.score.data.source.cloud.ShopScoreCloud;
import com.tokopedia.shop.score.data.source.disk.ShopScoreCache;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreFactory {


    private final ShopScoreCloud shopScoreCloud;
    private final ShopScoreCache shopScoreCache;
    private final ShopScoreDetailMapper shopScoreDetailMapper;

    @Inject
    public ShopScoreFactory(
            ShopScoreCloud shopScoreCloud,
            ShopScoreCache shopScoreCache,
            ShopScoreDetailMapper shopScoreDetailMapper
    ) {
        this.shopScoreCloud = shopScoreCloud;
        this.shopScoreCache = shopScoreCache;
        this.shopScoreDetailMapper = shopScoreDetailMapper;
    }

    public ShopScoreSummaryDataSource createShopScoreSummarySource() {
        return new ShopScoreSummaryDataSource(shopScoreCloud, shopScoreCache);
    }

    public ShopScoreDetailDataSource createShopScoreDetailSource() {
        return new ShopScoreDetailDataSource(shopScoreCloud, shopScoreCache, shopScoreDetailMapper);
    }
}

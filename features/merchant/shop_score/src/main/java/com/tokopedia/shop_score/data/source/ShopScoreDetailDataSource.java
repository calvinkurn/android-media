package com.tokopedia.shop_score.data.source;

import com.tokopedia.shop_score.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.shop_score.data.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.shop_score.data.source.cloud.ShopScoreCloud;
import com.tokopedia.shop_score.data.source.disk.ShopScoreCache;
import com.tokopedia.shop_score.domain.model.ShopScoreDetailDomainModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailDataSource {
    private final ShopScoreCloud shopScoreCloud;
    private final ShopScoreCache shopScoreCache;
    private final ShopScoreDetailMapper shopScoreDetailMapper;

    public ShopScoreDetailDataSource(ShopScoreCloud shopScoreCloud,
                                     ShopScoreCache shopScoreCache,
                                     ShopScoreDetailMapper shopScoreDetailMapper) {
        this.shopScoreCloud = shopScoreCloud;
        this.shopScoreCache = shopScoreCache;
        this.shopScoreDetailMapper = shopScoreDetailMapper;
    }

    public Observable<ShopScoreDetailDomainModel> getShopScoreDetail() {
        return shopScoreCache.getShopScoreDetail()
                .onErrorResumeNext(
                        shopScoreCloud.getShopScoreDetailData()
                                .doOnNext(new StoreShopDetailToCache())
                )
                .map(shopScoreDetailMapper);
    }

    private class StoreShopDetailToCache implements Action1<ShopScoreDetailServiceModel> {
        @Override
        public void call(ShopScoreDetailServiceModel serviceModel) {
            shopScoreCache.storeShopScoreDetail(serviceModel);
        }
    }
}

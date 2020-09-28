package com.tokopedia.shop.score.data.source.cloud;

import com.tokopedia.shop.score.data.common.GetData;
import com.tokopedia.shop.score.data.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.shop.score.data.model.summary.ShopScoreSummaryServiceModel;
import com.tokopedia.shop.score.domain.GoldMerchantApi;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreCloud {
    private final GoldMerchantApi api;
    private final UserSessionInterface sessionHandler;

    @Inject
    public ShopScoreCloud(GoldMerchantApi api, UserSessionInterface sessionHandler) {
        this.api = api;
        this.sessionHandler = sessionHandler;
    }

    public Observable<ShopScoreSummaryServiceModel> getShopScoreSummaryData() {
        return api
                .getShopScoreSummary(sessionHandler.getShopId())
                .map(new GetData<ShopScoreSummaryServiceModel>());
    }

    public Observable<ShopScoreDetailServiceModel> getShopScoreDetailData() {
        return api
                .getShopScoreDetail(sessionHandler.getShopId())
                .map(new GetData<ShopScoreDetailServiceModel>());
    }
}

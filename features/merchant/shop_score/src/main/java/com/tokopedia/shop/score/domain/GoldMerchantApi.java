package com.tokopedia.shop.score.domain;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.shop.score.data.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.shop.score.data.model.summary.ShopScoreSummaryServiceModel;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GoldMerchantApi {

    @GET(TkpdBaseURL.GoldMerchant.GET_SHOP_SCORE_SUMMARY + "{shopId}")
    Observable<Response<ShopScoreSummaryServiceModel>> getShopScoreSummary(@Path("shopId") String shopId);

    @GET(TkpdBaseURL.GoldMerchant.GET_SHOP_SCORE_DETAIL + "{shopId}")
    Observable<Response<ShopScoreDetailServiceModel>> getShopScoreDetail(@Path("shopId") String shopID);
}
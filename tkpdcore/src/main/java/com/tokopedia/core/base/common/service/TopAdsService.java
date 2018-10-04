package com.tokopedia.core.base.common.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.affiliateProductData.AffiliateProductDataResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public interface TopAdsService {

    @GET(TkpdBaseURL.TopAds.PATH_GET_PROMO_TOP_ADS)
    Observable<Response<String>> getTopAds(@QueryMap Map<String, Object> params);

    @GET(TkpdBaseURL.TopAds.PATH_GET_SHOP_TOP_ADS)
    Observable<Response<String>> getShopTopAds(@QueryMap Map<String, Object> params);

    @GET
    Observable<Response<String>> productWishlistUrl(@Url String wishlistUrl);

    @GET(TkpdBaseURL.TopAds.PATH_GET_PDP_AFFILIATE_DATA)
    Observable<Response<AffiliateProductDataResponse>> getPdpAffiliateData(
            @Query("ui") String ui,
            @Query("user_id") String userId,
            @Query("shop_id") String shopId,
            @Query("product_ids") String productIds
    );
}



package com.tokopedia.core.network.apiservices.goldmerchant.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.product.model.goldmerchant.FeaturedProductServiceModel;
import com.tokopedia.core.product.model.goldmerchant.ProductVideoData;
import com.tokopedia.core.product.model.shopscore.detail.ShopScoreDetailServiceModel;
import com.tokopedia.core.product.model.shopscore.summary.ShopScoreSummaryServiceModel;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by kris on 11/9/16. Tokopedia
 */

public interface GoldMerchantApi {

    @GET(TkpdBaseURL.GoldMerchant.GET_PRODUCT_VIDEO+"{productId}")
    Observable<Response<ProductVideoData>> fetchVideo(@Path("productId") String productId);

    @GET(TkpdBaseURL.GoldMerchant.GET_SHOP_SCORE_SUMMARY + "{shopId}")
    Observable<Response<ShopScoreSummaryServiceModel>> getShopScoreSummary(@Path("shopId") String shopId);

    @GET(TkpdBaseURL.GoldMerchant.GET_SHOP_SCORE_DETAIL + "{shopId}")
    Observable<Response<ShopScoreDetailServiceModel>> getShopScoreDetail(@Path("shopId") String shopID);

    @GET(TkpdBaseURL.GoldMerchant.GET_FEATURED_PRODUCTS)
    Observable<Response<FeaturedProductServiceModel>> getShopFeaturedProducts(@Path("shopId") String shopId);

}
package com.tokopedia.gm.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.data.source.cloud.model.GMGetCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.RequestGetCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface GMCommonApi {

    @GET(GMCommonUrl.FEATURED_PRODUCT_URL+"?json=1")
    Observable<Response<DataResponse<List<GMFeaturedProduct>>>> getFeaturedProductList(@Path("shop_id") String shopId);

    @POST(GMCommonUrl.SET_CASHBACK_PRODUCTS)
    Observable<Response<DataResponse<String>>> setCashback(@Body RequestCashbackModel cashback);

    @POST(GMCommonUrl.GET_CASHBACK_PRODUCTS)
    Observable<Response<DataResponse<List<GMGetCashbackModel>>>> getCashbackList(@Body RequestGetCashbackModel requestGetCashbackModel);

    @GET(GMCommonUrl.SHOPS_SCORE_STATUS + "{shopId}")
    Observable<Response<DataResponse<ShopScoreResult>>> getShopScoreDetail(@Path("shopId") String shopId);

}

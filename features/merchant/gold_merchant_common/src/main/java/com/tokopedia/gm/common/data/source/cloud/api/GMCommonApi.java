package com.tokopedia.gm.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.data.source.cloud.model.GMGetCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantActivationResult;
import com.tokopedia.gm.common.data.source.cloud.model.RequestAutoExtendPowerMerchantModel;
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.RequestGetCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

    @Headers("Origin: tokopedia.com")
    @GET(GMCommonUrl.GET_SHOP_STATUS)
    Observable<Response<DataResponse<ShopStatusModel>>> getShopStatus(@Path("shop_id") String shopId);

    // to activate power merchant
    @Headers("Origin: tokopedia.com")
    @POST(GMCommonUrl.SHOPS_SUBSCRIPTION)
    Observable<Response<DataResponse<PowerMerchantActivationResult>>> activatePowerMerchant();

    // to turn on/off powerMerchant
    @Headers("Origin: tokopedia.com")
    @POST(GMCommonUrl.SHOPS_SUBSCRIPTIONS_AUTO_EXTEND)
    Observable<Response<DataResponse<PowerMerchantActivationResult>>> turnOnOffPowerMerchantSubscription(
            @Body RequestAutoExtendPowerMerchantModel requestAutoExtendPowerMerchantModel);
}

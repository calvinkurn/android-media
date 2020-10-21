package com.tokopedia.core.network.apiservices.goldmerchant.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.product.model.goldmerchant.ProductVideoData;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by kris on 11/9/16. Tokopedia
 */

@Deprecated
public interface GoldMerchantApi {

    @GET(TkpdBaseURL.GoldMerchant.GET_PRODUCT_VIDEO+"{productId}")
    Observable<Response<ProductVideoData>> fetchVideo(@Path("productId") String productId);
}
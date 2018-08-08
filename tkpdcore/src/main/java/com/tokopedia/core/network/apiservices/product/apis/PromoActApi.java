package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * PromoActApi
 * Created by Angga.Prasetiyo on 08/12/2015.
 */
public interface PromoActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_AD_IMPRESSION_CLICK)
    Observable<Response<TkpdResponse>> adImpressionClick(@FieldMap Map<String, String> params);
}

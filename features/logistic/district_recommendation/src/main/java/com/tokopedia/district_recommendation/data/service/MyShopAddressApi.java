package com.tokopedia.district_recommendation.data.service;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface MyShopAddressApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_LOCATION)
    Observable<Response<TokopediaWsV4Response>> getLocation(@FieldMap Map<String, String> params);
}

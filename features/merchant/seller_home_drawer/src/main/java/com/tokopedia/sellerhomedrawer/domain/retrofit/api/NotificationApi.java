package com.tokopedia.sellerhomedrawer.domain.retrofit.api;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.sellerhomedrawer.data.constant.SellerDrawerUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

@Deprecated
public interface NotificationApi {

    @GET(SellerDrawerUrl.User.PATH_GET_NOTIFICATION)
    Observable<Response<TokopediaWsV4Response>> getNotification(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(SellerDrawerUrl.User.PATH_RESET_NOTIFICATION)
    Observable<Response<TokopediaWsV4Response>> resetNotification(@FieldMap Map<String, String> params);

    @GET(SellerDrawerUrl.User.PATH_GET_NOTIFICATION)
    Observable<Response<TokopediaWsV4Response>> getNotification2(@QueryMap TKPDMapParam<String, Object> params);
}
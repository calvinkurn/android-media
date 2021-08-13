package com.tokopedia.logisticCommon.data.apiservice;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticCommon.data.constant.LogisticDataConstantUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface PeopleActApi {

    @FormUrlEncoded
    @POST(LogisticDataConstantUrl.PeopleAction.PATH_EDIT_ADDRESS)
    Observable<String> editAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(LogisticDataConstantUrl.PeopleAction.PATH_EDIT_ADDRESS)
    Observable<Response<TokopediaWsV4Response>> editAddAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(LogisticDataConstantUrl.PeopleAction.PATH_EDIT_DEFAULT_ADDRESS)
    Observable<Response<TokopediaWsV4Response>> editDefaultAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(LogisticDataConstantUrl.PeopleAction.PATH_ADD_ADDRESS)
    Observable<Response<TokopediaWsV4Response>> addAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(LogisticDataConstantUrl.PeopleAction.PATH_DELETE_ADDRESS)
    Observable<Response<TokopediaWsV4Response>> deleteAddress(@FieldMap Map<String, String> params);

    @GET(LogisticDataConstantUrl.PeopleAction.PATH_GET_ADDRESS)
    Observable<Response<TokopediaWsV4Response>> getAddress(@QueryMap Map<String, String> params);

}

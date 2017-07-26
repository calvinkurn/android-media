package com.tokopedia.core.network.apiservices.payment.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public interface BcaOneClickApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Payment.PATH_ONE_CLICK_GET_ACCESS_TOKEN)
    Observable<Response<String>>
    getBcaOneClickAccessToken(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Payment.PATH_ONE_CLICK_GET_USER_DATA)
    Observable<Response<String>>
    getBcaOneClickUserData(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Payment.PATH_ONE_CLICK_ADD_USER_DATA)
    Observable<Response<String>>
    registerBcaOneClickUserData(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Payment.PATH_ONE_CLICK_EDIT_USER_DATA)
    Observable<Response<String>>
    editBcaOneClickUserData(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Payment.PATH_ONE_CLICK_DELETE_USER_DATA)
    Observable<Response<String>>
    deleteBcaOneClickUserData(@FieldMap TKPDMapParam<String, String> params);

}
